package com.example.ChatelainHeumelContacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ChatelainHeumelContacts.R;

public class Activity_ajout extends AppCompatActivity {


    private NotesDbAdapter mDbHelper;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajout);


        //Création de la bdd
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * @param view
     */
    public void enregistre(View view){

        //Boolean
        boolean choix;

        //nom
        final EditText ajouteNom = (EditText) findViewById(R.id.ajout_nom);
        String nom = ajouteNom.getText().toString();

        //prenom
        final EditText ajoutePrenom = (EditText) findViewById(R.id.ajout_prenom);
        String prenom = ajoutePrenom.getText().toString();

        //telephone
        final EditText ajouteTelephone = (EditText) findViewById(R.id.ajout_telephone);
        String telephone = ajouteTelephone.getText().toString();

        //mail
        final EditText ajouteMail = (EditText) findViewById(R.id.ajoutEmail);
        String mail = ajouteMail.getText().toString();

        //adresses
        final EditText ajouteAdresse = (EditText) findViewById(R.id.ajout_Adresse);
        String adresse = ajouteAdresse.getText().toString();

        //Favoris
        final CheckBox fav = (CheckBox) findViewById(R.id.ajoutfav);
        if(fav.isChecked()){
            choix = true;
        }else{
            choix = false;
        }

        mDbHelper.createContact(nom,prenom,telephone,mail,adresse,choix);

        //initialisation
        ajouteNom.setText("");
        ajoutePrenom.setText("");
        ajouteTelephone.setText("");
        ajouteMail.setText("");
        ajouteAdresse.setText("");

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }



}
