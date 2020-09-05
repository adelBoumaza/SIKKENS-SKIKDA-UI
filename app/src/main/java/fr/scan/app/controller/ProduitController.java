package fr.scan.app.controller;

import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import fr.scan.app.model.Produit;

public class ProduitController {

    public final static  String BASE_URL = "http://51.178.87.186/api/produit/";

    private ProduitController() {

    }

    /** Holder */
    private static class SingletonHolder
    {
        /** Instance unique non préinitialisée */
        private final static ProduitController instance = new ProduitController();
    }

    /** Point d'accès pour l'instance unique du singleton */
    public static ProduitController getInstance()
    {
        return SingletonHolder.instance;
    }


    public Produit saveOrUpdate(Produit produit) throws ExecutionException, InterruptedException {

        return new HttpRequestTaskSaveOrUpdate()
                .execute(produit)
                .get()
                .getBody();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public List<Produit> getAllProduit() {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Produit getProduitByReference(String ref) throws ExecutionException, InterruptedException {
        if(ref != null) {
           AsyncTask<String, Void, Produit> produit = new HttpRequestTaskFindByReference().execute(ref);
           if(produit != null) {
               return produit.get();
           }
        }
        return null;
    }

    public void deleteProduit(Produit produit) {
        new HttpRequestTaskDelete().execute(produit.getReference());
    }


    private class HttpRequestTaskFindByReference extends AsyncTask<String, Void, Produit> {

        @Override
        protected Produit doInBackground(String... params) {
            final  String reference = params[0];
            final String url = ProduitController.BASE_URL +reference;
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(
                    new MappingJackson2HttpMessageConverter());
             Produit produit = restTemplate.getForObject(url, Produit.class);
            return produit;
        }

    }


    private class HttpRequestTaskSaveOrUpdate extends AsyncTask<Produit, Void, ResponseEntity<Produit> >  {

        @Override
        protected ResponseEntity<Produit> doInBackground(Produit... produit) {
            final  Produit pr = produit[0];
            final String url = ProduitController.BASE_URL +"saveOrUpdate";
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(
                    new MappingJackson2HttpMessageConverter());
            HttpEntity<Produit> request = new HttpEntity<>(pr);
            ResponseEntity<Produit> produitRetour = restTemplate.exchange(url, HttpMethod.POST,request,Produit.class);
            return produitRetour;
        }

        @Override
        protected void onPostExecute(ResponseEntity<Produit> produitResponseEntity) {
            Produit produit = produitResponseEntity.getBody();
        }
    }

    private class HttpRequestTaskDelete extends AsyncTask<String, Void, Void > {

        @Override
        protected Void doInBackground(String... string) {
            final  String reference = string[0];
            Produit produit = new Produit();
            produit.setReference(reference);
            RestTemplate restTemplate = new RestTemplate();
            final String url = ProduitController.BASE_URL +"delete";
            restTemplate.getMessageConverters().add(
                    new MappingJackson2HttpMessageConverter());
            HttpEntity<Produit> request = new HttpEntity<>(produit);
            restTemplate.exchange(url, HttpMethod.DELETE,request,Produit.class);
            return null;
        }
    }
}
