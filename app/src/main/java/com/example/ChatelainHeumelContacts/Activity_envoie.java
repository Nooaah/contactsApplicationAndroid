package com.example.ChatelainHeumelContacts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ChatelainHeumelContacts.R;

public class Activity_envoie extends AppCompatActivity {


    String telephone;

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envoie);

        /*On recup le numero de téléphone*/
        Intent intent = getIntent();
        telephone = intent.getStringExtra("le_numero");



        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /**
     * Envoie un message dans un futur proche
     * @param view
     */
    public void envoye_message(View view){

        //On recup le message
        EditText editText = (EditText) findViewById(R.id.Aa___);
        String message = editText.getText().toString();


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setType("vnd.android-dir/mms-sms/");
        intent.putExtra("address", telephone);
        intent.putExtra("sms_body", message);
        startActivity(intent);
    }

}
