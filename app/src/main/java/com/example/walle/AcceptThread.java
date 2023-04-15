package com.example.walle;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

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
        BluetoothServerSocket tmp = null;
        try {
            if (ActivityCompat.checkSelfPermission(c, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            tmp = adapter.listenUsingRfcommWithServiceRecord("Name", uuid);
        } catch (Exception e) { e.printStackTrace(); }
        _socket = tmp;
    }

    public void run() {
        BluetoothSocket socket = null;
        // Keep listening until exception occurs or a socket is returned.
        while (true) {
            try {
                socket = _socket.accept();
            } catch (IOException e) {
                Log.e(TAG, "Socket's accept() method failed", e);
                break;
            }

            if (socket != null) {
                // A connection was accepted. Perform work associated with
                // the connection in a separate thread.
                manageMyConnectedSocket(socket);
                try {
                    _socket.close();
                } catch (Exception e) { e.printStackTrace(); }
                break;
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
            InputStream is = socket.getInputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String s = reader.readLine();
            Log.i(TAG, "Output -> " + s);
            ProgressDialog.show(c, s, "Data RECEIVED", true);
        } catch (Exception e) { }
    }
}