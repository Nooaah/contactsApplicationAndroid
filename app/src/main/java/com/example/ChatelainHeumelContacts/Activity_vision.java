package com.example.ChatelainHeumelContacts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.example.ChatelainHeumelContacts.R;

public class Activity_vision extends AppCompatActivity {


    private NotesDbAdapter mDbHelper;
    private TextView test;
    long ide;

    /**
     * Comme un main
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vision);

        Intent intent = getIntent();
        ide = intent.getExtras().getLong("ide");

        //Création de la bdd
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        Remplirfiche(ide);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Remplissage de la fiche contact
     */
    private void Remplirfiche(long id) {
        final ListView maVariableListView = (ListView) findViewById(R.id.listInformation);

        Cursor c = mDbHelper.fetchContacts(id);
        startManagingCursor(c);

        String[] from = new String[]{NotesDbAdapter.KEY_NOM, NotesDbAdapter.KEY_PRENOM,
                NotesDbAdapter.KEY_TELEPHONE, NotesDbAdapter.KEY_EMAIL, NotesDbAdapter.KEY_ADDRESSE,
                NotesDbAdapter.KEY_FAVORIS};

        int[] to = new int[]{R.id.fillNom, R.id.fillPrenom, R.id.fillTelephone, R.id.fillMail,
                R.id.fillAdresse, R.id.fillFavoris};

        SimpleCursorAdapter contact =
                new SimpleCursorAdapter(this, R.layout.contact_information, c, from, to);
        maVariableListView.setAdapter(contact);

    }

    /**
     * @param view
     */
    public void appel(View view) {
        TextView tel = (TextView) findViewById(R.id.fillTelephone);
        String tele = tel.getText().toString();

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + tele));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
    }

    /**
     * @param view
     */
    public void mail(View view){

        TextView mel = (TextView) findViewById(R.id.fillMail);
        String mail = mel.getText().toString();

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{mail});
        startActivity(Intent.createChooser(i, "Titre:"));
    }

    /**
     * @param view
     */
    public void localisation(View view){

        TextView loc = (TextView) findViewById(R.id.fillAdresse);
        String adresse = loc.getText().toString();

        Toast.makeText(this, "Recherche dans MAPS", Toast.LENGTH_SHORT).show();
        Uri location = Uri.parse("geo:0,0?q="+adresse);
        Intent webmaps = new Intent(Intent.ACTION_VIEW, location);
        startActivity(webmaps);
    }

    /**
     * @param view
     */
    public void sms(View view){

        TextView loc = (TextView) findViewById(R.id.fillTelephone);
        String tell = loc.getText().toString();

        Intent intent = new Intent(this, Activity_envoie.class);
        intent.putExtra("le_numero",tell);
        startActivity(intent);
    }

    /**
     * @param view
     */
    public void modif(View view){

        /*On recup le tout */
        TextView no = (TextView) findViewById(R.id.fillNom);
        String nom = no.getText().toString();

        TextView preno = (TextView) findViewById(R.id.fillPrenom);
        String prenom = preno.getText().toString();

        TextView phone = (TextView) findViewById(R.id.fillTelephone);
        String telephone = phone.getText().toString();

        TextView mail = (TextView) findViewById(R.id.fillMail);
        String email = mail.getText().toString();

        TextView add = (TextView) findViewById(R.id.fillAdresse);
        String addresse = add.getText().toString();

        TextView fav = (TextView) findViewById(R.id.fillFavoris);
        String favoris  = mail.getText().toString();

        Intent intentmodif = new Intent(this, Activity_modification.class);

        intentmodif.putExtra("modifid",ide);
        intentmodif.putExtra("modifnom",nom);
        intentmodif.putExtra("modifprenom",prenom );
        intentmodif.putExtra("modiftel",telephone);
        intentmodif.putExtra("modifmail",email);
        intentmodif.putExtra("modifadresse",addresse);
        startActivity(intentmodif);
    }


    /**
     * @param view
     */
    public void creer(View view){

        /*On recup tout */
        TextView no = (TextView) findViewById(R.id.fillNom);
        String nom = no.getText().toString();

        TextView preno = (TextView) findViewById(R.id.fillPrenom);
        String prenom = preno.getText().toString();

        TextView phone = (TextView) findViewById(R.id.fillTelephone);
        String telephone = phone.getText().toString();

        TextView mail = (TextView) findViewById(R.id.fillMail);
        String email = mail.getText().toString();

        TextView add = (TextView) findViewById(R.id.fillAdresse);
        String addresse = add.getText().toString();


        Intent intentqr = new Intent(this, CreaQRCode.class);

        intentqr.putExtra("qrid",ide);
        intentqr.putExtra("qrnom",nom);
        intentqr.putExtra("qrprenom",prenom );
        intentqr.putExtra("qrtel",telephone);
        intentqr.putExtra("qrmail",email);
        intentqr.putExtra("qradresse",addresse);
        startActivity(intentqr);
    }
}
