package com.example.tam110.communication.bluetooth;

import android.app.ActivityManager;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND;
import static android.app.ActivityManager.RunningAppProcessInfo.IMPORTANCE_VISIBLE;

class BluetoothStateChange
{
    public static final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();

            //Log.i("BluetoothINFO", "stateOff: "+BluetoothAdapter.STATE_OFF);

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        //Toast.makeText(context.getApplicationContext(), "Bluetooth off", Toast.LENGTH_LONG).show();



                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        context.startActivity(enableBtIntent);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        //Toast.makeText(context.getApplicationContext(), "Turning Bluetooth off...", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context.getApplicationContext(), "Bluetooth turned on :)", Toast.LENGTH_LONG).show();
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        //Toast.makeText(context.getApplicationContext(), "Turning Bluetooth on...", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        }
    };
}
