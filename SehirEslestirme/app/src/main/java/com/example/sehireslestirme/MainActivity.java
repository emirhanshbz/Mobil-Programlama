package com.example.sehireslestirme;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView listPlaka, listSehir;
    private Button btnYenile;
    private ArrayAdapter<String> plakaAdapter, sehirAdapter;
    private List<String> plakaList, sehirList;
    private Map<String, String> plakaSehirMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listPlaka = findViewById(R.id.listPlaka);
        listSehir = findViewById(R.id.listSehir);
        btnYenile = findViewById(R.id.btnYenile);

        plakaSehirMap = new HashMap<>();
        initializePlakaSehir();

        plakaList = new ArrayList<>();
        sehirList = new ArrayList<>();

        plakaAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, plakaList);
        sehirAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, sehirList);

        listPlaka.setAdapter(plakaAdapter);
        listSehir.setAdapter(sehirAdapter);

        generateRandomLists();

        btnYenile.setOnClickListener(v -> generateRandomLists());

        listSehir.setOnItemClickListener((parent, view, position, id) -> {
            if (position < plakaList.size()) {
                String selectedPlaka = plakaList.get(position);
                String selectedSehir = sehirList.get(position);
                Intent intent = new Intent(MainActivity.this, SehirKontrol.class);
                intent.putExtra("plaka", selectedPlaka);
                intent.putExtra("sehir", selectedSehir);
                startActivity(intent);
            }
        });
    }

    public Map<String, String> getPlakaSehirMap() {
        return plakaSehirMap;
    }

    private void initializePlakaSehir() {
        String[] sehirler = {"Adana", "Adıyaman", "Afyon", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin",
                "Aydın", "Balıkesir", "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa",
                "Çanakkale", "Çankırı", "Çorum", "Denizli", "Diyarbakır", "Edirne", "Elazığ", "Erzincan",
                "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari", "Hatay", "Isparta",
                "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
                "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla",
                "Muş", "Nevşehir", "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop",
                "Sivas", "Tekirdağ", "Tokat", "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van",
                "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman", "Kırıkkale", "Batman", "Şırnak",
                "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"};

        for (int i = 0; i < 81; i++) {
            String plaka = String.format("%02d", i + 1);
            plakaSehirMap.put(plaka, sehirler[i]);
        }
    }

    private void generateRandomLists() {
        List<String> plakalar = new ArrayList<>(plakaSehirMap.keySet());
        List<String> sehirler = new ArrayList<>(plakaSehirMap.values());

        Collections.shuffle(plakalar);
        Collections.shuffle(sehirler);

        plakaList.clear();
        sehirList.clear();

        for (int i = 0; i < 10; i++) {
            plakaList.add(plakalar.get(i));
            sehirList.add(sehirler.get(i));
        }

        plakaAdapter.notifyDataSetChanged();
        sehirAdapter.notifyDataSetChanged();
    }
}
