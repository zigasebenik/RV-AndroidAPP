package com.example.tam110.communication.bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.companion.AssociationRequest;
import android.companion.BluetoothDeviceFilter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.example.tam110.MainActivity;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


public class BluetoothWriteReadIntentService extends IntentService
{

    public final static int REQUEST_ENABLE_BT = 1;
    public static final String ACTIVITY = "ACTIVITY";
    public static final String ALERT = "ALERT";
    public static final String WRITE_DATA = "WRITE_DATA";
    public static final String DEVICE_POSITION = "BLE_DATA";
    public static final String DEVICE_NAME = "DEVICE_ID";
    public static final String READ_DATA = "READ_DATA";
    public static final String CHARASTERISTIC_READ_RESULT = "CHARASTERISTIC_READ_RESULT";


    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private final static String TAG = BluetoothWriteReadIntentService.class.getSimpleName();


    private int position;
    private String deviceName;


    public BluetoothWriteReadIntentService()
    {
        super("BluetoothConnection");
    }

    private void sendData(String deviceName, int value)
    {
        this.position = value;
        this.deviceName = deviceName;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            showToast("Bluetooth ni na voljo v napravi.");
            throw new RuntimeException("NO BLUETOOTH");
        }

        if (!bluetoothAdapter.isEnabled())
        {
            showToast("Za delovanje je treba prižgati bluetooth.");
        }
        else
        {
            //BLUETOOTH ENABLED

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            boolean serverPaired = false;

            if (pairedDevices.size() > 0)
            {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices)
                {
                    String serverName = device.getName();
                    String serverHardwareAddress = device.getAddress();
                    Log.i("TEST", serverHardwareAddress);

                    if (serverName.equals("TAM-110-50b0bd5c-c67b") && serverHardwareAddress.equals("30:AE:A4:9C:C8:4E"))
                    {
                        //CONNECT to SERVER
                        if(connectionState == STATE_DISCONNECTED)
                            bluetoothGatt = device.connectGatt(this, true, gattWriteCallback);

                        serverPaired = true;
                    }
                }
            }

            if(serverPaired == false)
            {
                if(bluetoothAdapter.isEnabled())
                {
                    notBondedYet();
                }
            }

        }
    }

    private void readData(String deviceName, int value)
    {
        this.position = value;
        this.deviceName = deviceName;

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            showToast("Bluetooth ni na voljo v napravi.");
            throw new RuntimeException("NO BLUETOOTH");
        }

        if (!bluetoothAdapter.isEnabled())
        {
            showToast("Za delovanje je treba prižgati bluetooth.");
        }
        else
        {
            //BLUETOOTH ENABLED

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
            boolean serverPaired = false;

            if (pairedDevices.size() > 0)
            {
                // There are paired devices. Get the name and address of each paired device.
                for (BluetoothDevice device : pairedDevices)
                {
                    String serverName = device.getName();
                    String serverHardwareAddress = device.getAddress();
                    Log.i("TEST", serverHardwareAddress);

                    if (serverName.equals("TAM-110-50b0bd5c-c67b") && serverHardwareAddress.equals("30:AE:A4:9C:C8:4E"))
                    {
                        //CONNECT to SERVER
                        if(connectionState == STATE_DISCONNECTED)
                            bluetoothGatt = device.connectGatt(this, true, gattReadCallback);

                        serverPaired = true;
                    }
                }
            }

            if(serverPaired == false)
            {
                if(bluetoothAdapter.isEnabled())
                {
                    notBondedYet();
                }
            }

        }
    }

    private AssociationRequest pairingRequest;
    private BluetoothDeviceFilter deviceFilter;

    ScanCallback scanCallback = new ScanCallback()
    {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onScanResult(int callbackType, ScanResult result)
        {
            super.onScanResult(callbackType, result);
            BluetoothDevice device = result.getDevice();

            if(device.getName().equals("TAM-110-50b0bd5c-c67b"))
            {
                device.createBond();


            }
        }

        @Override
        public void onScanFailed(int errorCode)
        {
            super.onScanFailed(errorCode);
            showToast("Ne najdem server naprave :(");
            bluetoothLeScanner.stopScan(scanCallback);
        }

        @Override
        public void onBatchScanResults(List<ScanResult> results)
        {
            super.onBatchScanResults(results);
        }
    };


    final BluetoothGattCallback gattWriteCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                connectionState = STATE_CONNECTED;
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:" + bluetoothGatt.discoverServices());

            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                connectionState = STATE_DISCONNECTED;
                Intent alert = new Intent(getBaseContext(), MainActivity.class);
                alert.setAction(ALERT);
                alert.addFlags(FLAG_ACTIVITY_NEW_TASK);
                alert.putExtra(DEVICE_POSITION, "Bluetooth povezava prekinjena.");
                startActivity(alert);
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {

                BluetoothGattService service = gatt.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"));
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"));
                characteristic.setValue(String.valueOf(position));
                bluetoothGatt.writeCharacteristic(characteristic);
            }
            else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic)
        {
            super.onCharacteristicChanged(gatt, characteristic);
            String value = new String(characteristic.getValue());
            if(value.equals("ON"))
                showToast(deviceName+" prižgana");
            else if(value.equals("OFF"))
                showToast(deviceName+" ugasnjena");
        }
    };


    final BluetoothGattCallback gattReadCallback = new BluetoothGattCallback()
    {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState)
        {
            String intentAction;
            if (newState == BluetoothProfile.STATE_CONNECTED)
            {
                connectionState = STATE_CONNECTED;
                Log.i(TAG, "Connected to GATT server.");
                Log.i(TAG, "Attempting to start service discovery:" + bluetoothGatt.discoverServices());

            }
            else if (newState == BluetoothProfile.STATE_DISCONNECTED)
            {
                connectionState = STATE_DISCONNECTED;
                Intent alert = new Intent(getBaseContext(), MainActivity.class);
                alert.setAction(ALERT);
                alert.addFlags(FLAG_ACTIVITY_NEW_TASK);
                alert.putExtra(DEVICE_POSITION, "Bluetooth povezava prekinjena.");
                startActivity(alert);
            }
        }

        @Override
        // New services discovered
        public void onServicesDiscovered(BluetoothGatt gatt, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {

                BluetoothGattService service = gatt.getService(UUID.fromString("4fafc201-1fb5-459e-8fcc-c5c9c331914b"));
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(UUID.fromString("beb5483e-36e1-4688-b7f5-ea07361b26a8"));
                bluetoothGatt.readCharacteristic(characteristic);
            }
            else
            {
                Log.w(TAG, "onServicesDiscovered received: " + status);
            }
        }

        @Override
        // Result of a characteristic read operation
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status)
        {
            if (status == BluetoothGatt.GATT_SUCCESS)
            {
                String value = new String(characteristic.getValue());
                if(value.equals("ON"))
                    showToast(deviceName+" prižgana");
                else if(value.equals("OFF"))
                    showToast(deviceName+" ugasnjena");

                Intent intent = new Intent("UpdateMessageIntent");
                intent.putExtra(DEVICE_POSITION, position);
                intent.putExtra(READ_DATA, new String(characteristic.getValue()));
                sendBroadcast(intent);
            }
        }
    };


    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        String action = intent.getAction();

        if (action.equals(WRITE_DATA))
        {
            int value = intent.getIntExtra(DEVICE_POSITION, 0);
            String deviceName = intent.getStringExtra(DEVICE_NAME);
            sendData(deviceName, value);
        }
        else if (action.equals(READ_DATA))
        {
            int value = intent.getIntExtra(DEVICE_POSITION, 0);
            String deviceName = intent.getStringExtra(DEVICE_NAME);
            readData(deviceName, value);
        }
    }

    public void showToast(String message) {
        final String msg = message;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    void notBondedYet()
    {
        if (!this.getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            showToast("Tehnologija za komunikacijo ni podprta v napravi.");
        }

        Intent alert = new Intent(this, MainActivity.class);
        alert.setAction(ALERT);
        alert.addFlags(FLAG_ACTIVITY_NEW_TASK);
        alert.putExtra(DEVICE_POSITION, "Za pridobitev PINa se je potrebno povezati na:\n\n WiFi: TAM-110\n Geslo: floyd ni na morju\n\nV brskalnik je treba vpisati:\n 192.168.4.1\n Koda velja 30 sekund od prikaza za vnos PINa. Za pridobitev nove kode je potrebno vzpostaviti povezavo preko blutootha, da se prikaže nov vnos PINa, nato pa je treba osvežiti spletno stran (192.168.4.1).\nPovezavo z WiFijem je najlažje delati na drugi napravi.");
        startActivity(alert);



        bluetoothLeScanner= bluetoothAdapter.getBluetoothLeScanner();

        ScanFilter.Builder builder = new ScanFilter.Builder();
        builder.setDeviceName("TAM-110-50b0bd5c-c67b");
        ScanFilter scanFilter = builder.build();


        ScanSettings.Builder builderScanSettings = new ScanSettings.Builder();
        builderScanSettings.setScanMode(ScanSettings.CALLBACK_TYPE_FIRST_MATCH);
        ScanSettings scanSettings = builderScanSettings.build();

        final Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                bluetoothLeScanner.flushPendingScanResults(scanCallback);
                bluetoothLeScanner.stopScan(scanCallback);
            }
        }, 100);

        bluetoothLeScanner.startScan(Collections.singletonList(scanFilter), scanSettings, scanCallback);

    }

}
