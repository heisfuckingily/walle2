package com.example.walle;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.util.ArraySet;
import android.widget.Button;

import java.util.Set;
import java.util.UUID;

public class BluetoothListActivity extends AppCompatActivity {
    private Button back;
    private static final UUID Uuid_Insecure = UUID.fromString("de8a3f06-73b1-48df-89cc-6db78f31d64f");

    public static final Set<BluetoothDevice> devicesList = new ArraySet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = (Button) findViewById(R.id.backBtn);
        setupClickListeners();

        initList();
    }

    private void setupClickListeners() {
        back.setOnClickListener((e) -> {
            setContentView(R.layout.activity_main);
        });
    }

    private void init() {
        BluetoothManager btmanager = getSystemService(BluetoothManager.class);
        BluetoothAdapter adapter = btmanager.getAdapter();
        int requestCode = 1;
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, requestCode);

        if (adapter == null) {
        }
        if (!adapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableBtIntent, );
            // enable bt here
        }

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.BLUETOOTH_CONNECT}, 1);
            return;
        }
        Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
        pairedDevices.forEach(device -> {
            devicesList.add(device);
            if (!device.getName().equals("OSmething")) {
                String deviceHardwareAddress = device.getAddress(); // MAC ADDRESS
                new AcceptThread(BluetoothListActivity.this, adapter, Uuid_Insecure);
            }
        });
    }

    private void initList() {

    }
}