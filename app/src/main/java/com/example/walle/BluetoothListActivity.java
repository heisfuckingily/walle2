package com.example.walle;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.ArraySet;
import android.widget.Button;
import android.widget.Toast;

import java.util.Set;
import java.util.UUID;

public class BluetoothListActivity extends AppCompatActivity {
    private Button back;
    private Button c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        back = findViewById(R.id.backBtn);
        c = findViewById(R.id.connectBtn);
        setupClickListeners();
    }

    private void setupClickListeners() {
        back.setOnClickListener((e) -> {
            setContentView(R.layout.activity_main);
        });
        c.setOnClickListener((e) -> {

        });
    }




}