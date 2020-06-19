package com.example.tam110;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tam110.communication.bluetooth.BluetoothConnection;
import com.example.tam110.ui.main.devices.DevicesFragment;
import com.example.tam110.ui.main.devices.data.DeviceData;
import com.example.tam110.ui.main.lights.LightsFragment;
import com.example.tam110.ui.main.lights.data.LightsData;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tam110.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements DevicesFragment.OnListFragmentInteractionListener, LightsFragment.OnListFragmentInteractionListener
{


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());

        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, BluetoothConnection.REQUEST_ENABLE_BT);

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

    }

    @Override
    public void onListFragmentInteraction(DeviceData.Device item)
    {

    }

    @Override
    public void onListFragmentInteraction(LightsData.Light item)
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothConnection.REQUEST_ENABLE_BT)
        {
            if (resultCode == RESULT_OK)
            {
                Log.i("BluetoothCOMPLETE", "ENABLED");
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Log.i("BluetoothCOMPLETE", "CANCELED");
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, BluetoothConnection.REQUEST_ENABLE_BT);
            }
        }
    }

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