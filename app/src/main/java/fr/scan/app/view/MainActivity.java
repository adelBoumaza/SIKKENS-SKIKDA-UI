package fr.scan.app.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import fr.scan.app.controller.ProduitController;
import fr.scan.app.model.CaptureAct;
import fr.scan.app.R;
import fr.scan.app.model.Produit;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity  implements View.OnClickListener {

    private Button scanBtnVendre;
    private Button scanBtnInserer;
    private Button btnListProduit;
    private ProduitController produitController = ProduitController.getInstance();
    private AtomicInteger atomicInteger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        atomicInteger = new AtomicInteger(0);
        scanBtnVendre  = findViewById(R.id.btnConsulter);
        scanBtnInserer  = findViewById(R.id.btnInserer);
        btnListProduit  = findViewById(R.id.btnListProduit);
        scanBtnVendre.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
               atomicInteger.set(1);
               sccannerMoi();
            }
        });
        scanBtnInserer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atomicInteger.set(0);
                sccannerMoi();
            }
        });
        goToPageHistorique();

    }

    public void goToPageHistorique() {
        btnListProduit.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListProduitActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        sccannerMoi();
    }

    private void sccannerMoi() {
        IntentIntegrator intentIntegrator = new IntentIntegrator(this);
        intentIntegrator.setCaptureActivity(CaptureAct.class);
        intentIntegrator.setOrientationLocked(false);
        intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        intentIntegrator.setPrompt("Scanning code");
        intentIntegrator.initiateScan();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        Produit produit = null;
        if(intentResult != null) {
            if(intentResult.getContents() != null) {
                try {
                    produit = produitController.getProduitByReference(intentResult.getContents());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // vendre le produit
                if(atomicInteger.get() == 1 && produit != null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    AlertDialog dialog = null;
                    builder.setMessage("Réf : " + intentResult.getContents());
                      if(produit.getQuantite() > 0) {
                          try {
                              produit.setNotAddition(true);
                              produit = produitController.saveOrUpdate(produit);
                          } catch (Exception e) {
                              e.printStackTrace();
                          }
                          builder.setTitle("Le stock à bien modifié, il reste "+produit.getQuantite()+ " Pr");
                          dialog = builder.create();
                          builder.setPositiveButton("", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  finish();
                              }
                          }).setNegativeButton("Scanning terminé", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  finish();
                              }
                          });
                      }else{
                          builder.setTitle("Le stock à bien modifié, il reste "+produit.getQuantite()+ " Pr");
                          dialog = builder.create();
                      }
                    dialog.show();
                    atomicInteger.set(0);
                    return;
                }
                Intent page = null;
                if(produit != null) {
                    page = new Intent(MainActivity.this, ListProduitActivity.class);
                }else{
                    page = new Intent(MainActivity.this, InsererOrModifierActivity.class);
                }
                page.putExtra("reference", intentResult.getContents());
                startActivity(page);
                /*AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(intentResult.getContents());
                builder.setTitle("Sccaning result");
                builder.setPositiveButton("scan encore", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sccannerMoi();
                    }
                }).setNegativeButton("Scanning terminé", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();*/
            }else{
                Toast.makeText(this, "Pas de résultat", Toast.LENGTH_LONG).show();
            }
        }else {
            super.onActivityResult(requestCode,resultCode, data);
        }
    }
}