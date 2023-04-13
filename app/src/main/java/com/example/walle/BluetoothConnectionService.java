package com.example.walle;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.nio.charset.Charset;
import java.util.UUID;

public class BluetoothConnectionService {
    private static final String Tag = "BluetoothConnectionServ";
    private static final String Name = "WALL_E_2";
    private static final UUID Uuid_Insecure = UUID.fromString("de8a3f06-73b1-48df-89cc-6db78f31d64f");
    private static final UUID Uuid_Secure = UUID.fromString("35273cde-bce7-47ff-8ced-29fb5e7962a5");

    private final BluetoothAdapter _bluetoothAdapter;
    Context _context;

    private AcceptThread _insecureAcceptThread;
    private ConnectThread _connectThread;
    private BluetoothDevice _bluetoothDevice;
    private UUID _deviceUuid;
    ProgressDialog _progressDialog;

    private ConnectedThread _connectedThread;

    public BluetoothConnectionService(Context _context) {
        this._context = _context;
        this._bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Class AcceptThread
     */
    private class AcceptThread extends Thread {
        private BluetoothServerSocket _serverSocket;

        public AcceptThread() {
            BluetoothServerSocket tmp = null;

            try {
                if (ActivityCompat.checkSelfPermission(_context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    return;
                tmp = _bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(Name, Uuid_Insecure);
                Log.d(Tag, "AcceptThread: Setting up Server using " + Uuid_Insecure);
            } catch (Exception ex) {
                Log.e(Tag, "AcceptThread!", ex);
                return;
            }

            _serverSocket = tmp;

        }

        @Override
        public void run() {
            Log.d(Tag, "run: AcceptThread running.");
            BluetoothSocket socket = null;

            try {
                Log.d(Tag, "run: RFCOM server socket start....");
                socket = _serverSocket.accept();
                Log.d(Tag, "run: RFCOM server socket connection has been accepted!");
            } catch (IOException e) {
                Log.e(Tag, "AcceptThread: IOEXCEPTION", e);
            }

            if (socket != null) {
                connected(socket, _bluetoothDevice);
            }
            Log.i(Tag, "END _acceptThread");
        }

        public void cancel() {
            Log.d(Tag, "cancel: Cancelling AcceptThread!");
            try {
                _serverSocket.close();
            } catch (IOException ignored) { }
        }
    }

    /**
     * ConnectThread class
     */
    private class ConnectThread extends Thread {
        private BluetoothSocket _socket;

        public ConnectThread(BluetoothDevice device, UUID id) {
            _bluetoothDevice = device;
            _deviceUuid = id;
            Log.d(Tag, "ConnectThread: connect thread has been started!");
        }

        @Override
        public void run() {
            BluetoothSocket tmp = null;
            Log.i(Tag, "RUN _connectThread");
            // bedoon tozihat
            try {
                if (ActivityCompat.checkSelfPermission(_context, android.Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED)
                    return;
                tmp = _bluetoothDevice.createRfcommSocketToServiceRecord(_deviceUuid);
            } catch (Exception e) {
                Log.e(Tag, "E", e);
            }

            _socket = tmp;

            _bluetoothAdapter.cancelDiscovery();
            try {
                _socket.connect();

                Log.d(Tag, "run: Socket connected!");
            } catch (Exception e) { try { _socket.close(); } catch (Exception ignored) { } e.printStackTrace(); }

            connected(_socket, _bluetoothDevice);
        }

        public void cancel() {
            Log.d(Tag, "cancel: Cancelling AcceptThread!");
            try {
                _socket.close();
            } catch (IOException ignored) { }
        }
    }

    /**
     * Class ConnectedThread
     */
    private class ConnectedThread extends Thread {
        private final BluetoothSocket _socket;
        private final InputStream _is;
        private final OutputStream _os;

        public ConnectedThread(BluetoothSocket so) {
            _socket = so;
            InputStream tmpI = null;
            OutputStream tmpO = null;

            _progressDialog.dismiss();

            try {
                tmpI = _socket.getInputStream();
                tmpO = _socket.getOutputStream();
            } catch (Exception ignored) { }
            _is = tmpI;
            _os = tmpO;
        }

        @Override
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = _is.read(buffer);
                    String incM = new String(buffer, 0, bytes);
                    Log.i(Tag, "Incoming Message ---> " + incM);
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
        }

        public void cancel() {
            Log.d(Tag, "cancel: Cancelling AcceptThread!");
            try {
                _socket.close();
            } catch (IOException ignored) { }
        }

        public void write(byte[] bytes) {
            String text = new String(bytes, Charset.defaultCharset());
            try {
                _os.write(bytes);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void start() {
        Log.d(Tag, "Start....");
        if (_connectThread != null) {
            _connectThread.cancel();
            _connectThread = null;
        }
        if (_insecureAcceptThread == null) {
            _insecureAcceptThread = new AcceptThread();
            _insecureAcceptThread.start();
        }
    }

    public void startClient(BluetoothDevice dev, UUID uid) {
        _progressDialog = ProgressDialog.show(_context, "Connecting bluetooth", "Please wait...", true);
        _connectThread = new ConnectThread(dev, uid);
        _connectThread.start();
    }

    private void connected(BluetoothSocket _socket, BluetoothDevice _device) {
        _connectedThread = new ConnectedThread(_socket);
        _connectedThread.start();
    }

    public void write(byte[] out) {
        _connectedThread.write(out);
    }
}
