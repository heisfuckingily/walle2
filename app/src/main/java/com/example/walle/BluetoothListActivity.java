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



    private void initList() {

    }
}