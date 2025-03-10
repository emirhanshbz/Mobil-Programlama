package com.example.hafta32;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ListView list1 = (ListView) findViewById(R.id.list1);
        TextView text1 = (TextView) findViewById(R.id.textView2);

        String[] sehirler = {"İstanbul", "Ankara", "Antalya", "İzmir"};
        ArrayAdapter<String> veriListesi = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, sehirler);
        list1.setAdapter(veriListesi);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, veriListesi.getItem(i), Toast.LENGTH_SHORT).show();
                text1.setText(veriListesi.getItem(i));
            }
        });
    }
}