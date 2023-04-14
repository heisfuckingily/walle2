package com.example.walle;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import androidx.core.app.ActivityCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

import kotlin.Metadata;
import kotlin.NotImplementedError;
import kotlin.jvm.internal.Intrinsics;

@Metadata(
        mv = {1, 8, 0},
        k = 1,
        d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0010\t\n\u0000\u0018\u00002\u00020\u00012\u00020\u0002B\u0005¢\u0006\u0002\u0010\u0003J\u0016\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u00072\u0006\u0010\u000b\u001a\u00020\fJ\u0012\u0010\r\u001a\u00020\t2\b\u0010\u000e\u001a\u0004\u0018\u00010\u000fH\u0014J0\u0010\u0010\u001a\u00020\t2\f\u0010\u0011\u001a\b\u0012\u0002\b\u0003\u0018\u00010\u00122\b\u0010\u0013\u001a\u0004\u0018\u00010\u00142\u0006\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0019"},
        d2 = {"Lcom/example/walle/MainActivity;", "Landroidx/appcompat/app/AppCompatActivity;", "Landroid/widget/AdapterView$OnItemClickListener;", "()V", "_bluetoothConnectionService", "Lcom/example/walle/BluetoothConnectionService;", "_bluetoothDevice", "Landroid/bluetooth/BluetoothDevice;", "StartBluetoothConnection", "", "device", "uuid", "Ljava/util/UUID;", "onCreate", "savedInstanceState", "Landroid/os/Bundle;", "onItemClick", "p0", "Landroid/widget/AdapterView;", "p1", "Landroid/view/View;", "p2", "", "p3", "", "app_debug"}
)
public final class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String Tag = "MainActivity";

    Button _btnStartConnection;

    private BluetoothConnectionService _bluetoothConnectionService;
    private BluetoothDevice _bluetoothDevice;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        this._btnStartConnection = (Button) findViewById(R.id.btnStartConnection);

        this._bluetoothConnectionService = new BluetoothConnectionService(this);
        this.registerReceiver(mBRC1, new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED));

        _btnStartConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartConnection();
            }
        });
    }

    public void onItemClick(@Nullable AdapterView p0, @Nullable View p1, int p2, long p3) {
        String var6 = "Not yet implemented";
        throw new NotImplementedError("An operation is not implemented: " + var6);
    }

    private void StartConnection() {
        StartBluetoothConnection(_bluetoothDevice, UUID.fromString("de8a3f06-73b1-48df-89cc-6db78f31d64f"));
    }

    public void StartBluetoothConnection(BluetoothDevice device, UUID uuid) {
        BluetoothConnectionService var10000 = this._bluetoothConnectionService;
        var10000.startClient(device, uuid);
    }

    private final BroadcastReceiver mBRC1 = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)) {
                BluetoothDevice dev = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    return;
                switch (dev.getBondState()) {
                    case BluetoothDevice.BOND_BONDED:
                        Log.d(Tag, "Connected TO RFCOM");
                        _bluetoothDevice = dev;
                        break;
                    case BluetoothDevice.BOND_BONDING:
                        Log.d(Tag, "BOND_BONDING");
                        break;
                    case BluetoothDevice.BOND_NONE:
                        Log.d(Tag, "BOND_NONE");
                        break;
                }
            }
        }
    };
}





