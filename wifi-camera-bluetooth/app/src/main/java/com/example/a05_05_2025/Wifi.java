package com.example.a05_05_2025;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

public class Wifi extends AppCompatActivity {

    private WifiManager wifiManager;
    private ToggleButton toggleButton;
    private static final int REQUEST_CHANGE_WIFI_STATE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        toggleButton = findViewById(R.id.toggleButton);

        if (wifiManager == null) {
            Toast.makeText(this, "Wi-Fi servisi alınamadı.", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        updateToggleButtonState();


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setWifiState(isChecked);
            }
        });
    }

    private void updateToggleButtonState() {
        if (wifiManager != null) {
            toggleButton.setChecked(wifiManager.isWifiEnabled());
        }
    }

    private void setWifiState(boolean enableWifi) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            Toast.makeText(this, "Wi-Fi'ı değiştirmek için lütfen Ayarlar panelini kullanın.", Toast.LENGTH_LONG).show();
            Intent panelIntent = new Intent(Settings.Panel.ACTION_WIFI);
            startActivityForResult(panelIntent, 545);
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_STATE) == PackageManager.PERMISSION_GRANTED) {
                boolean success = wifiManager.setWifiEnabled(enableWifi);
                if (success) {
                    Toast.makeText(this, enableWifi ? "Wi-Fi Açıldı" : "Wi-Fi Kapatıldı", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Wi-Fi durumu değiştirilemedi.", Toast.LENGTH_SHORT).show();
                    updateToggleButtonState();
                }

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CHANGE_WIFI_STATE},
                        REQUEST_CHANGE_WIFI_STATE_PERMISSION);
                updateToggleButtonState();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 545 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            updateToggleButtonState();
        }
    }


    // İzin isteği sonucunu işleme
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CHANGE_WIFI_STATE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setWifiState(toggleButton.isChecked());
            } else {
                Toast.makeText(this, "Wi-Fi durumu değiştirme izni verilmedi.", Toast.LENGTH_SHORT).show();
                updateToggleButtonState();
            }
        }
    }
}
