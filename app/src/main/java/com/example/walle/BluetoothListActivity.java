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
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.ListFragment;

import java.util.ArrayList;
import java.util.Collections;

public class BluetoothListActivity extends AppCompatActivity {
    public static String BT_DEV_NAME = "HC_05";

    private Button back;
    private Button c;

    private BluetoothAdapter bluetoothAdapter;
    private final Map<String, BluetoothDevice> listItems = new HashMap<>();
    private boolean permissionMissing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH))
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        refresh();
        TextUtil.status(this, "RUN REFREISHDW!!!!!!!!!!!");


        back = findViewById(R.id.backBtn);
        c = findViewById(R.id.connectBtn);
        setupClickListeners();
        this.setContentView(R.layout.activity_bluetooth_list);
    }

    private void refresh() {
        if(bluetoothAdapter != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                permissionMissing = this.checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED;
            }
            if(!permissionMissing) {
                for (BluetoothDevice device : bluetoothAdapter.getBondedDevices())
                    if (device.getType() != BluetoothDevice.DEVICE_TYPE_LE) {
                        listItems.put(device.getName(), device);
                        TextUtil.status(this, device.getAddress());
                    }
            }
        }
    }

    private void setupClickListeners() {
        back.setOnClickListener((e) -> {
            Log.i("WHAT", "WHAT IS HAPPENING WHAT WHAT");
            this.setContentView(R.layout.activity_main);
        });
        c.setOnClickListener((e) -> {

        });
    }




}