package fr.scan.app.adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import fr.scan.app.R;
import fr.scan.app.controller.ProduitController;
import fr.scan.app.model.Produit;

public class ListAdapter extends BaseAdapter {

    private ProduitController produitController = ProduitController.getInstance();

    private Context context;
    private List<Produit> produitList;
    private TextView reference;
    private TextView quantite;
    private TextView prix;
    private Button btnAdd;
    private Button btnSous;
    private Button btnDelete;


    private void init(View convertView) {
        btnAdd = convertView.findViewById(R.id.btnAdd);
        btnDelete = convertView.findViewById(R.id.btnDelete);
        btnSous = convertView.findViewById(R.id.btnSous);
    }

    public ListAdapter() {
    }

    public ListAdapter(Context context, List<Produit> produitList) {
        this.context = context;
        this.produitList = produitList;
    }

    @Override
    public int getCount() {
        return produitList.size();
    }

    @Override
    public Object getItem(int i) {
        return produitList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View rowView = View.inflate(context, R.layout.item_product_list, null);
        init(rowView);
        reference = rowView.findViewById(R.id.tbReference);
        quantite = rowView.findViewById(R.id.tbQuantite);
        //prix = rowView.findViewById(R.id.tbPrix);
        reference.setText(produitList.get(i).getReference());
        quantite.setText(String.valueOf(produitList.get(i).getQuantite()));
        //prix.setText( String.valueOf((produitList.get(i).getPrix()).intValue()));
        rowView.setTag(produitList.get(i));
        add(rowView);
        sous(rowView);
        delete(rowView);
        return rowView;
    }



    public void add(final View rowView) {
        btnAdd.setOnClickListener(new Button.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Produit produit = (Produit) rowView.getTag();
                try {
                    Produit newProduit = produitController.saveOrUpdate(produit);
                    produitList = Arrays.asList(newProduit);
                    notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void sous(View rowView) {
        btnSous.setOnClickListener(new View.OnClickListener(){
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Produit produit = (Produit) rowView.getTag();
                try {
                    if(produit.getQuantite() > 0) {
                        produit.setNotAddition(true);
                        Produit newProduit = produitController.saveOrUpdate(produit);
                        produitList = Arrays.asList(newProduit);
                        notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void delete(View rowView) {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {
                Produit produit = (Produit) rowView.getTag();
                produitController.deleteProduit(produit);
                notifyDataSetChanged();
            }
        });
    }


}
