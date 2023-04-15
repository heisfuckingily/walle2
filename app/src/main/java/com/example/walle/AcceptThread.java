package com.example.walle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

public class AcceptThread extends Thread {
    public static final String TAG = "AcceptThread";

    private Context c;

    private BluetoothServerSocket _socket;

    public AcceptThread(Context c, BluetoothAdapter adapter, UUID uuid) {
        this.c = c;
        Toast.makeText(c, "Debug 1", Toast.LENGTH_SHORT).show();
        BluetoothServerSocket tmp = null;
        try {
            if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(c, "Debug 123t127638712371554158e24", Toast.LENGTH_SHORT).show();
            }
            tmp = adapter.listenUsingRfcommWithServiceRecord("Name", uuid);
        } catch (Exception e) {
            Toast.makeText(c, "EX: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        _socket = tmp;
    }

    public void run() {
        Looper.prepare();
        Toast.makeText(c, "Debug RUNUNRURNRUNRUNRUNRUNRUNRU RUN NIGGAAA", Toast.LENGTH_SHORT).show();
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = _socket.accept();
                Toast.makeText(c, "Debug ACCEPTQ!@#!@", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                Toast.makeText(c, "EX: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                Toast.makeText(c, "Debug 3", Toast.LENGTH_SHORT).show();
                try {
                    _socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(c, "EX: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    // Closes the connect socket and causes the thread to finish.
    public void cancel() {
        try {
            _socket.close();
        } catch (IOException e) {
            Log.e(TAG, "Could not close the connect socket", e);
        }
    }

    public void manageMyConnectedSocket(BluetoothSocket socket) {
        try {
            Toast.makeText(c, "Debug 5", Toast.LENGTH_SHORT).show();

            InputStream is = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s = reader.readLine();
            Log.i(TAG, "Output -> " + s);
            Toast.makeText(c, "Output ----> " + s, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(c, "EX: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
