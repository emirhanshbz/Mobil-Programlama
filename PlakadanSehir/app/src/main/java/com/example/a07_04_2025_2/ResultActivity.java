package com.example.a07_04_2025_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ResultActivity extends AppCompatActivity {

    ListView listeSonuclar;
    Button btnGeriDon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        listeSonuclar = findViewById(R.id.listeSonuclar);
        btnGeriDon = findViewById(R.id.btnGeriDon);

        ArrayList<String> sonucListesi = getIntent().getStringArrayListExtra("sonuclar");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
          this,
          android.R.layout.simple_list_item_1,
            sonucListesi
        );

        listeSonuclar.setAdapter(adapter);

        btnGeriDon.setOnClickListener(v -> {
            Intent intent = new Intent(ResultActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
    }
}

