package fr.scan.app.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import fr.scan.app.R;
import fr.scan.app.controller.ProduitController;
import fr.scan.app.model.Produit;

public class InsererOrModifierActivity extends AppCompatActivity {

    private Button scanBtnValider;
    private Button scanBtnRetour;
    private EditText referenceProduit;
    private EditText nombre;
    private EditText prix;

    private ProduitController produitController = ProduitController.getInstance();

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserer_or_modifier);
        init();
        Bundle extras = getIntent().getExtras();
        String reference = extras.getString("reference");
        referenceProduit.setText(reference);
        createProduit();
        retourToScan();
    }

    private void init() {
        nombre = findViewById(R.id.nbrNombre);
        nombre.setText(String.valueOf(1));
        prix = findViewById(R.id.nbrPrix);
        referenceProduit= findViewById(R.id.txtReference);
        scanBtnValider = findViewById(R.id.btnValider);
        scanBtnRetour = findViewById(R.id.btnRetour);
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public  void createProduit() { // #607d8b
        scanBtnValider.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Produit produit = new Produit();
                produit.setReference(referenceProduit.getText().toString());
                produit.setQuantite(Integer.parseInt(nombre.getText().toString()));
                produit.setPrix(BigDecimal.valueOf(Integer.parseInt(prix.getText().toString())));
                try {
                    produitController.saveOrUpdate(produit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("produit", "onClick: "+produit);
                AlertDialog.Builder builder = new AlertDialog.Builder(InsererOrModifierActivity.this);
                builder.setTitle("Le produit sous la référence "+produit.getReference()+" à bien été ajouté avec succès");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public  void retourToScan() {
        scanBtnRetour.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InsererOrModifierActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}