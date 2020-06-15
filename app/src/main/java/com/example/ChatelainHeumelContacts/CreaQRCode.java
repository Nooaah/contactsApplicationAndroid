package com.example.ChatelainHeumelContacts;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.ChatelainHeumelContacts.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

public class CreaQRCode extends AppCompatActivity {

    private ImageView imageView;
    private String liste;
    private Long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_qrcode);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Je recup toutes les infos
        Intent intent = getIntent();
        id = intent.getExtras().getLong("qrid");
        String nom = intent.getExtras().getString("qrnom");
        String prenom = intent.getExtras().getString("qrprenom");
        String telephone = intent.getExtras().getString("qrtel");
        String mail = intent.getExtras().getString("qrmail");
        String adresse = intent.getExtras().getString("qradresse");

        liste = nom+","+prenom+","+telephone+","+mail+","+adresse;
        imageView = findViewById(R.id.imageView);
        creer();
    }

    public void creer(){

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        try {
            BitMatrix bitMatrix = qrCodeWriter.encode(liste, BarcodeFormat.QR_CODE, 200,200);
            Bitmap bitmap = Bitmap.createBitmap(200,200,Bitmap.Config.RGB_565);

            for(int x=0;x<200;x++){
                for(int y=0;y<200;y++){
                    bitmap.setPixel(x,y,bitMatrix.get(x,y)? Color.BLACK : Color.WHITE);
                }
            }

            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
