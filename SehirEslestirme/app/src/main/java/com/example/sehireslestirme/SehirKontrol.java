package com.example.sehireslestirme;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

public class SehirKontrol extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_layout);

        TextView txtSonuc = findViewById(R.id.txtSonuc);

        Intent intent = getIntent();
        String plaka = intent.getStringExtra("plaka");
        String sehir = intent.getStringExtra("sehir");

        MainActivity mainActivity = new MainActivity();
        Map<String, String> plakaSehirMap = mainActivity.getPlakaSehirMap();

        if (plakaSehirMap.get(plaka).equals(sehir)) {
            Toast.makeText(this, "Doğru!", Toast.LENGTH_SHORT).show();
        } else {
            String dogruSehir = plakaSehirMap.get(plaka);
            Toast.makeText(this, "Yanlış! Doğru Plaka: " + plaka + " - " + dogruSehir, Toast.LENGTH_LONG).show();
        }
    }
}
