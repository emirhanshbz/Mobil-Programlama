package com.example.a07_04_2025_2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Button btnBaslat, btnOnayla;
    SeekBar plakaSecici;
    EditText sehirGirisi;
    TextView textSure, textPlaka;

    long baslamaZamani;
    boolean sayacAktif = false;
    Handler zamanlayici = new Handler();
    Runnable sureGuncelle;

    int[][] plakaVerileri = {
            {1, "adana".hashCode()},
            {6, "ankara".hashCode()},
            {34, "istanbul".hashCode()},
            {35, "izmir".hashCode()},
            {16, "bursa".hashCode()},
            {41, "kocaeli".hashCode()}
            // Yeni şehir eklemek istersen buraya ekle
    };

    ArrayList<String> dogruSonuclar = new ArrayList<>();
    int dogruSayisi = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnBaslat = findViewById(R.id.btnBaslat);
        btnOnayla = findViewById(R.id.btnOnayla);
        plakaSecici = findViewById(R.id.plakaSecici);
        sehirGirisi = findViewById(R.id.sehirGirisi);
        textSure = findViewById(R.id.textSure);
        textPlaka = findViewById(R.id.textPlaka);

        textPlaka.setText("Seçilen plaka: 1");

        sureGuncelle = new Runnable() {
            @Override
            public void run() {
                if (sayacAktif) {
                    long gecen = (System.currentTimeMillis() - baslamaZamani) / 1000;
                    textSure.setText("Süre: " + gecen + " saniye");
                    zamanlayici.postDelayed(this, 1000);
                }
            }
        };


        btnBaslat.setOnClickListener(v -> {
            baslamaZamani = System.currentTimeMillis();
            sayacAktif = true;
            zamanlayici.post(sureGuncelle);
            Toast.makeText(this, "Süre başladı", Toast.LENGTH_SHORT).show();
        });

        plakaSecici.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textPlaka.setText("Seçilen plaka: " + (progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        btnOnayla.setOnClickListener(v -> {
            int secilenPlaka = plakaSecici.getProgress() + 1;
            String girilenSehir = sehirGirisi.getText().toString().trim().toLowerCase();

            String dogruSehir = plakadanSehirGetir(secilenPlaka);

            if (dogruSehir != null && girilenSehir.equals(dogruSehir)) {
                sayacAktif = false;
                long gecenSure = (System.currentTimeMillis() - baslamaZamani) / 1000;

                dogruSonuclar.add(dogruSehir + "  " + gecenSure + " saniye");
                dogruSayisi++;

                Intent intent = new Intent(MainActivity.this, ResultActivity.class);
                intent.putStringArrayListExtra("sonuclar", dogruSonuclar);
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Yanlış şehir adı!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    String plakadanSehirGetir(int plakaNo) {
        for (int[] veri : plakaVerileri) {
            if (veri[0] == plakaNo) {
                for (String sehir : new String[]{"adana", "ankara", "istanbul", "izmir", "bursa", "kocaeli"}) {
                    if (veri[1] == sehir.hashCode()) {
                        return sehir;
                    }
                }
            }
        }
        return null;
    }
}
