package com.example.tam110.communication.bluetooth;

import android.app.Activity;
import android.app.ActivityOptions;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.widget.Toast;

import java.util.Set;


public class BluetoothConnection
{

    public final static int REQUEST_ENABLE_BT = 1;

    private static BluetoothAdapter turnOnBluetooth(Activity activity)
    {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(activity, "Bluetooth not supported in device.", Toast.LENGTH_LONG).show();
            throw new RuntimeException("NO BLUETOOTH");
        }

        if (!bluetoothAdapter.isEnabled()) {
            activity.startActivityForResult(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE), REQUEST_ENABLE_BT);
        }

        return bluetoothAdapter;
    }

    public static void setupConnection(Activity activity)
    {
        BluetoothAdapter bluetoothAdapter = turnOnBluetooth(activity);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

        activity.registerReceiver(BluetoothStateChange.mReceiver, filter);

        /*Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // There are paired devices. Get the name and address of each paired device.
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
                Log.i("TEST", deviceHardwareAddress);
            }
        }
        else
            Log.i("TEST", "NO PAIRED DEVICES");*/

    }

    public static void reTryBluetoothEnable(Activity activity)
    {
        turnOnBluetooth(activity);
    }
}
