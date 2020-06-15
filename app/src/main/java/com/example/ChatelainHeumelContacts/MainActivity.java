package com.example.ChatelainHeumelContacts;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ChatelainHeumelContacts.R;

public class MainActivity extends AppCompatActivity {

    private NotesDbAdapter mDbHelper;
    public ListView contactListView;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instanciations
        contactListView = (ListView) findViewById(R.id.listContact);
        registerForContextMenu(contactListView);

        //Création de la bdd
        mDbHelper = new NotesDbAdapter(this);
        mDbHelper.open();
        fillData();

        //Barre d'action
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(getApplicationContext(), Activity_vision.class);

                intent.putExtra("ide",id);
                startActivity(intent);
            }
        });



    }

    /**
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            fillDatawhere();
            return true;
        }
        if(id == R.id.all){
            fillData();
            return true;
        }
        if (id == R.id.QRCODE){
            Intent intent = new Intent(this, ScanQRCode.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * @param menu
     * @param v
     * @param menuInfo
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }

    /**
     * @param item
     * @return des actions différentes
     */
    @Override
    public boolean onContextItemSelected(final MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)
                item.getMenuInfo();
        Cursor SelectedTaskCursor = (Cursor) contactListView.getItemAtPosition(info.position);

        //Recuperer les valeurs utiles au fonctions precedente

        final long SelectedTaskId =
                SelectedTaskCursor.getLong(SelectedTaskCursor.getColumnIndex("_id"));

        final String SelectedTaskTelephone =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("telephone"));

        final String SelectedTaskAdresse =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("addresse"));

        final String SelectedTastMail =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("email"));

        final String SelectedTastNom =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("nom"));

        final String SelectedTaskPrenom =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("prenom"));

        final String SelectTaskFavoris =
                SelectedTaskCursor.getString(SelectedTaskCursor.getColumnIndex("favoris"));

        switch (item.getItemId()) {

            //La supression d'une ligne
            case R.id.suppression:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Voulez-vous tout supprimer ?");
                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mDbHelper.deleteContact(SelectedTaskId);
                        fillData();
                    }
                });
                builder.setNegativeButton(R.string.No, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
                return true;

            //Permet d'appeler
            case R.id.Appel:
                String tel = SelectedTaskTelephone;// Votre numéro de téléphone
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + tel));
                startActivity(callIntent);
                return true;

            //Envoie un sms
            case R.id.SMS:
                Intent intent = new Intent(this, Activity_envoie.class);
                /*On se retouve de l'autre côté*/
                intent.putExtra("le_numero",SelectedTaskTelephone);
                startActivity(intent);
                return true;

             //Envoie un email
            case R.id.CONMAIL:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("message/rfc822");
                i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{SelectedTastMail});
                startActivity(Intent.createChooser(i, "Titre:"));
                return true;

             //Recherche l'adresse
            case R.id.CONADD:
                Toast.makeText(this, "Recherche dans MAPS", Toast.LENGTH_SHORT).show();
                Uri location = Uri.parse("geo:0,0?q="+SelectedTaskAdresse);
                Intent webmaps = new Intent(Intent.ACTION_VIEW, location);
                startActivity(webmaps);
                return true;

             //Permet la modification des elements présent
            case R.id.CONMODIF:
                Intent intentmodif = new Intent(this, Activity_modification.class);

                intentmodif.putExtra("modifid",SelectedTaskId);
                intentmodif.putExtra("modifnom",SelectedTastNom);
                intentmodif.putExtra("modifprenom",SelectedTaskPrenom);
                intentmodif.putExtra("modiftel",SelectedTaskTelephone);
                intentmodif.putExtra("modifmail",SelectedTastMail);
                intentmodif.putExtra("modifadresse",SelectedTaskAdresse);
                startActivity(intentmodif);
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    /**
     * @param view
     */
    public void enAjout(View view){
        Intent intent = new Intent(this, Activity_ajout.class);
        startActivity(intent);
    }

    /**
     * Remplissage de ma liste
     */
    private void fillData() {
        final ListView maVariableListView = (ListView) findViewById(R.id.listContact);
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllContacts();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_NOM ,NotesDbAdapter.KEY_PRENOM};
        int[] to = new int[] { R.id.Contact_row_nom, R.id.Contact_row_prenom};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contact =
                new SimpleCursorAdapter(this, R.layout.contact_row, c, from, to);
        maVariableListView.setAdapter(contact);
    }

    /**
     * Remplissage de la liste avec des favoris
     */
    private void fillDatawhere() {
        final ListView maVariableListView = (ListView) findViewById(R.id.listContact);
        // Get all of the notes from the database and create the item list
        Cursor c = mDbHelper.fetchAllContactsFav();
        startManagingCursor(c);

        String[] from = new String[] { NotesDbAdapter.KEY_NOM ,NotesDbAdapter.KEY_PRENOM};
        int[] to = new int[] { R.id.Contact_row_nom, R.id.Contact_row_prenom};

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter contact =
                new SimpleCursorAdapter(this, R.layout.contact_row, c, from, to);
        maVariableListView.setAdapter(contact);
    }
}
