package com.example.ChatelainHeumelContacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ChatelainHeumelContacts.R;

public class Activity_modification extends AppCompatActivity {


    private NotesDbAdapter mDbHelper;
    long id;
    boolean choix;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);

        //on recup toutes les infos
        Intent intent = getIntent();
        id = intent.getExtras().getLong("modifid");
        String nom = intent.getExtras().getString("modifnom");
        String prenom = intent.getExtras().getString("modifprenom");
        String telephone = intent.getExtras().getString("modiftel");
        String mail = intent.getExtras().getString("modifmail");
        String adresse = intent.getExtras().getString("modifadresse");

        //Je les met sur mes text
        EditText modifnom = findViewById(R.id.ModifNom);
        modifnom.setText(nom);
        EditText modifprenom = findViewById(R.id.ModifPrenom);
        modifprenom.setText(prenom);
        EditText modiftelephone = findViewById(R.id.ModifTel);
        modiftelephone.setText(telephone);
        EditText modifmail = findViewById(R.id.ModifMail);
        modifmail.setText(mail);
        EditText modifadresse = findViewById(R.id.Modifadresse);
        modifadresse.setText(adresse);

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
    public void modifier(View view){

        //nom
        final EditText modifNom = (EditText) findViewById(R.id.ModifNom);
        String nom2 = modifNom.getText().toString();

        //prenom
        final EditText modifPrenom = (EditText) findViewById(R.id.ModifPrenom);
        String prenom2 = modifPrenom.getText().toString();

        //telephone
        final EditText modifTelephone = (EditText) findViewById(R.id.ModifTel);
        String telephone2 = modifTelephone.getText().toString();

        //mail
        final EditText modifMail = (EditText) findViewById(R.id.ModifMail);
        String mail2 = modifMail.getText().toString();

        //adresses
        final EditText modifAdresse = (EditText) findViewById(R.id.Modifadresse);
        String adresse2 = modifAdresse.getText().toString();

        //Favoris
        final CheckBox fav = (CheckBox) findViewById(R.id.Modiffav);
        if(fav.isChecked()){
            choix = true;
        }else{
            choix = false;
        }

        mDbHelper.updateContact(id,nom2,prenom2,telephone2,mail2,adresse2,choix);

        //Initialisation
        modifNom.setText("");
        modifPrenom.setText("");
        modifTelephone.setText("");
        modifMail.setText("");
        modifAdresse.setText("");

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

}
