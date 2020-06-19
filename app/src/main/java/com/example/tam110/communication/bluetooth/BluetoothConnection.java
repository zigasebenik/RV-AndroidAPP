package com.example.tam110.communication.bluetooth;

import android.app.IntentService;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;

import static android.bluetooth.BluetoothProfile.STATE_CONNECTED;
import static android.telecom.Call.STATE_DISCONNECTED;


public class BluetoothConnection extends IntentService
{

    public final static int REQUEST_ENABLE_BT = 1;
    public static final String ACTIVITY = "ACTIVITY";
    public static final String SEND_TO_SERVER = "SETUP_BLEUTOOTH";
    public static final String BLE_DATA = "BLE_DATA";
    public static final String DEVICE_ID = "DEVICE_ID";

    private BluetoothManager bluetoothManager;
    private BluetoothAdapter bluetoothAdapter;
    private String bluetoothDeviceAddress;
    private BluetoothGatt bluetoothGatt;
    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;
    private final static String TAG = BluetoothConnection.class.getSimpleName();

    public BluetoothConnection()
    {
        super("BluetoothConnection");
    }

    private void sendData(String deviceName, int value)
    {

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null)
        {
            Toast.makeText(getBaseContext(), "Bluetooth not supported in device.", Toast.LENGTH_LONG).show();
            throw new RuntimeException("NO BLUETOOTH");
        }

        if (!bluetoothAdapter.isEnabled())
        {
            Toast.makeText(getBaseContext(), "Enable bluetooth.", Toast.LENGTH_LONG).show();
        }
        else
        {
            //BLUETOOTH ENABLED

            Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

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
                            bluetoothGatt = device.connectGatt(this, true, gattCallback);

                    }
                }
            }
            else
                Log.i("TEST", "NO PAIRED DEVICES");

            /*final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (bluetoothAdapter == null) {
                Toast.makeText(activity, "Bluetooth not supported in device.", Toast.LENGTH_LONG).show();
                throw new RuntimeException("NO BLUETOOTH");
            }

            if (!bluetoothAdapter.isEnabled()) {
                activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
            }

            if(bluetoothAdapter.isEnabled())
            {
                if (!activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
                    Toast.makeText(activity, "Cant connect to server: TECHNOLOGY NOT SUPPORTED", Toast.LENGTH_SHORT).show();
                    activity.finish();
                }

                final Handler handler = new Handler();

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bluetoothAdapter.stopLeScan(leScanCallback);
                    }
                }, 10000);

                bluetoothAdapter.startLeScan(leScanCallback);

            }*/
        }
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent)
    {
        String action = intent.getAction();

        if (action.equals(SEND_TO_SERVER))
        {
            int value = intent.getIntExtra(BLE_DATA, 0);
            String deviceName = intent.getStringExtra(DEVICE_ID);
            sendData(deviceName, value);
        }
    }


    final BluetoothGattCallback gattCallback = new BluetoothGattCallback()
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
                Log.i(TAG, "Disconnected from GATT server.");
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
                characteristic.setValue("kekw");
                bluetoothGatt.writeCharacteristic(characteristic);
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

            }
        }
    };
}
