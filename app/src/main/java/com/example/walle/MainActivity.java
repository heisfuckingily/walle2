package com.example.walle;

import android.Manifest;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;
import java.util.UUID;

import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
        mv = {1, 8, 0},
        k = 1,
        d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fJ\u0012\u0010\r\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J0\u0010\u0010\u001a\u00020\t2\f\u0010\u0011\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0019"},
        d2 = {"Lcom/example/walle/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;" , "Landroid/widget/AdapterView$OnItemClickListener;", "()V", "_bluetoothConnectionService", "Lcom/example/walle/BluetoothConnectionService;", "_bluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "StartBluetoothConnection", "", "device", "uuid", "Ljava/util/UUID;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onItemClick", "p0", "Landroid/widget/AdapterView;", "p1", "Landroid/view/View;", "p2", "", "p3", "", "app_debug"}
)
public final class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String Tag = "MainActivity";
    private static final UUID Uuid_Insecure = UUID.fromString("de8a3f06-73b1-48df-89cc-6db78f31d64f");

    private static final int BT1_PERMISSION_CODE = 1001;
    private static final int BT2_PERMISSION_CODE = 1002;
    private static final int BT3_PERMISSION_CODE = 1003;
    private static final int BT4_PERMISSION_CODE = 1004;
    private static final int BT5_PERMISSION_CODE = 1005;

    public static final Set<BluetoothDevice> devicesList = new ArraySet<>();

    Button _btnStartConnection;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this._btnStartConnection = (Button) findViewById(R.id.btnStartConnection);

        _btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBt();
            }
        });
    }

    public void onItemClick(@Nullable AdapterView p0, @Nullable View p1, int p2, long p3) {
        String var6 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var6);
    }

    private void initBt() {
        BluetoothManager btmanager = getSystemService(BluetoothManager.class);
        BluetoothAdapter adapter = btmanager.getAdapter();
        int requestCode = 1;
        Intent discoverableIntent =
                new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
        startActivityForResult(discoverableIntent, requestCode);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(MainActivity.this, "Permissions not granted right now", Toast.LENGTH_SHORT).show();
            checkPermission( android.Manifest.permission.BLUETOOTH_ADMIN, BT4_PERMISSION_CODE);
            checkPermission( android.Manifest.permission.BLUETOOTH, BT1_PERMISSION_CODE);
            checkPermission( android.Manifest.permission.ACCESS_FINE_LOCATION, BT2_PERMISSION_CODE);
            checkPermission( android.Manifest.permission.ACCESS_COARSE_LOCATION, BT3_PERMISSION_CODE);
            checkPermission( android.Manifest.permission.BLUETOOTH_SCAN, BT5_PERMISSION_CODE);
            checkPermission( Manifest.permission.BLUETOOTH_CONNECT, 2);
        }
        ProgressDialog.show(this, "Accepting now!", "Waiting for data...", true);
        new AcceptThread(MainActivity.this, adapter, Uuid_Insecure);
    }

    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}





