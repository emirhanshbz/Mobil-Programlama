package com.example.a05_05_2025;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {

    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 1;

    Button b1, b2, b3, b4;
    private BluetoothAdapter BA;
    private Set<BluetoothDevice> pairedDevices;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

        b1 = (Button) findViewById(R.id.button1);
        b2 = (Button) findViewById(R.id.button2);
        b3 = (Button) findViewById(R.id.button3);
        b4 = (Button) findViewById(R.id.button4);
        lv = (ListView) findViewById(R.id.listView);

        BA = BluetoothAdapter.getDefaultAdapter();

        if (BA == null) {
            Toast.makeText(getApplicationContext(), "Bluetooth cihazda desteklenmiyor", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        checkAndRequestPermissions();
    }

    // Gerekli Bluetooth izinlerini kontrol et ve iste
    private void checkAndRequestPermissions() {
        ArrayList<String> permissionsToRequest = new ArrayList<>();

        // Android 12 ve üzeri için yeni Bluetooth izinleri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_SCAN);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_CONNECT);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE);
            }
        } else { // Android 11 ve altı için eski izinler
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH);
            }
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                permissionsToRequest.add(Manifest.permission.BLUETOOTH_ADMIN);
            }
            // ACCESS_FINE_LOCATION izni de bazen Bluetooth taraması için gereklidir (özellikle Android 6-11 arası)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
            }
        }


        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToRequest.toArray(new String[0]), REQUEST_BLUETOOTH_PERMISSIONS);
        } else {
            initializeBluetooth();
        }
    }


    private void initializeBluetooth() {

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                on(v);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                off(v);
            }
        });

        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list(v);
            }
        });

        b4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeVisible(v);
            }
        });
    }

    public void on(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth açma izni (CONNECT) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth açma izni (BLUETOOTH/ADMIN) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        }

        if (!BA.isEnabled()) {
            Intent turnOn = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnOn, 0);
            Toast.makeText(getApplicationContext(), "Bluetooth Açılıyor...", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Zaten Açık", Toast.LENGTH_LONG).show();
        }
    }

    public void off(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth kapatma izni (CONNECT) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Bluetooth kapatma izni (ADMIN) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        }


        if (BA.isEnabled()) {
            BA.disable();
            Toast.makeText(getApplicationContext(), "Bluetooth Kapatıldı", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getApplicationContext(), "Bluetooth Zaten Kapalı", Toast.LENGTH_LONG).show();
        }
    }

    public void list(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Eşleşen cihazları listeleme izni (CONNECT) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Eşleşen cihazları listeleme izni (BLUETOOTH) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        }


        pairedDevices = BA.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        if (pairedDevices.size() > 0) {
            for (BluetoothDevice bt : pairedDevices) {
                String deviceName = bt.getName();
                String deviceAddress = bt.getAddress();
                if (deviceName == null) deviceName = "Bilinmeyen Cihaz";
                list.add(deviceName + "\n" + deviceAddress);

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(getApplicationContext(), "Eşleştirilmiş cihaz yok", Toast.LENGTH_LONG).show();
            lv.setAdapter(null);
        }
    }

    public void makeVisible(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADVERTISE) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Cihazı görünür yapma izni (ADVERTISE) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        } else {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_ADMIN) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Cihazı görünür yapma izni (ADMIN) verilmedi.", Toast.LENGTH_SHORT).show();
                checkAndRequestPermissions();
                return;
            }
        }


        Intent getVisible = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        startActivityForResult(getVisible, 0);
        Toast.makeText(getApplicationContext(), "Cihaz görünür hale getiriliyor...", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            boolean allPermissionsGranted = true;
            if (grantResults.length > 0) {
                for (int grantResult : grantResults) {
                    if (grantResult != PackageManager.PERMISSION_GRANTED) {
                        allPermissionsGranted = false;
                        break;
                    }
                }

                if (allPermissionsGranted) {
                    initializeBluetooth();
                } else {
                    Toast.makeText(this, "Bluetooth işlemi için gerekli izinler verilmedi.", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "İzin isteği sonucu alınamadı.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Bluetooth başarıyla açıldı", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Kullanıcı Bluetooth'u açmayı reddetti", Toast.LENGTH_SHORT).show();
            }
        }
        else if (requestCode == 0) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Görünürlük isteği iptal edildi veya reddedildi.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Cihaz " + resultCode + " saniye görünür olacak.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}