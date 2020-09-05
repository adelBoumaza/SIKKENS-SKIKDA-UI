package fr.scan.app.view;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import fr.scan.app.R;
import fr.scan.app.adapter.ListAdapter;
import fr.scan.app.controller.ProduitController;
import fr.scan.app.model.Produit;

public class ListProduitActivity extends AppCompatActivity {

    private ProduitController produitController = ProduitController.getInstance();
    private ListView listView;
    private ListAdapter adapter;
    private List<Produit> produitList;
    private Button btnRetourAcc;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_produit);
        Bundle extras = getIntent().getExtras();
        String reference = extras.getString("reference");
        View header = getLayoutInflater().inflate(R.layout.header, null);
        listView = (ListView) findViewById(R.id.listview_produit);
        btnRetourAcc = findViewById(R.id.btnRetourAcc);
        try {
            Produit produit = produitController.getProduitByReference(reference);
            produitList = Arrays.asList(produit);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        adapter = new ListAdapter(getApplicationContext(), produitList);
        listView.addHeaderView(header);
        listView.setAdapter(adapter);
        goToPageAccueil();
    }


    public void goToPageAccueil() {
        btnRetourAcc.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListProduitActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}