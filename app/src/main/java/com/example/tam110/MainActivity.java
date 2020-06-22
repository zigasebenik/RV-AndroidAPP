package com.example.tam110;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.example.tam110.communication.bluetooth.BluetoothWriteReadIntentService;
import com.example.tam110.ui.main.devices.DevicesFragment;
import com.example.tam110.ui.main.devices.data.DeviceData;
import com.example.tam110.ui.main.lights.LightsFragment;
import com.example.tam110.ui.main.lights.data.LightsData;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tam110.ui.main.SectionsPagerAdapter;

public class MainActivity extends AppCompatActivity implements DevicesFragment.OnListFragmentInteractionListener, LightsFragment.OnListFragmentInteractionListener
{

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;


    BluetoothWriteReadIntentService mService;
    boolean mBound = false;

    @RequiresApi(api = Build.VERSION_CODES.Q)
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

        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        this.registerReceiver(mReceiver, filter);

        if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
        {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_BACKGROUND_LOCATION);
            }
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_BACKGROUND_LOCATION}, PERMISSION_REQUEST_FINE_LOCATION);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(this, BluetoothWriteReadIntentService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
        startActivityForResult(enableBtIntent, BluetoothWriteReadIntentService.REQUEST_ENABLE_BT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    public void onRequestPermissionsResults(int requestCode, String[] permissions, int[] grantResults)
    {
        switch (requestCode)
        {
            case PERMISSION_REQUEST_BACKGROUND_LOCATION:
            case PERMISSION_REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    Toast.makeText(this, "Dovoljenja uspšno dodana", Toast.LENGTH_LONG).show();
                }
                else
                {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("TAM - 110");
                    builder.setMessage("Aplikacija potrebuje dovoljenja za normalno delovanje.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener()
                    {
                        @Override
                        public void onDismiss(DialogInterface dialog)
                        {
                        }
                    });
                    builder.show();
                }
                return;
        }
        // Other 'case' lines to check for other
        // permissions this app might request.
    }

    boolean alertShowing = false;

    @Override
    protected void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);

        if(alertShowing == false && intent.getAction().equals(BluetoothWriteReadIntentService.ALERT))
        {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Obvestilo");
            builder.setMessage(intent.getStringExtra(BluetoothWriteReadIntentService.DEVICE_POSITION));
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
                    alertShowing = false;
                }

            });

            builder.show();
            alertShowing = true;
        }

    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == BluetoothWriteReadIntentService.REQUEST_ENABLE_BT)
        {
            if (resultCode == RESULT_OK)
            {
                Log.i("BluetoothCOMPLETE", "ENABLED");
            }
            else if (resultCode == RESULT_CANCELED)
            {
                Log.i("BluetoothCOMPLETE", "CANCELED");
                Toast.makeText(this, "Aplikacija potrebuje bluetooth za delovanje", Toast.LENGTH_LONG).show();
            }
        }
    }

    public final BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            final String action = intent.getAction();

            //Log.i("BluetoothINFO", "stateOff: "+BluetoothAdapter.STATE_OFF);

            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED))
            {
                final int state = intent.getIntExtra(
                        BluetoothAdapter.EXTRA_STATE,
                        BluetoothAdapter.ERROR);
                switch (state)
                {
                    case BluetoothAdapter.STATE_OFF:
                        //Toast.makeText(context.getApplicationContext(), "Bluetooth off", Toast.LENGTH_LONG).show();
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        context.startActivity(enableBtIntent);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        Toast.makeText(context.getApplicationContext(), "Bluetooth prižgan :)", Toast.LENGTH_LONG).show();
                        mService.connectWithServerDevice();
                        break;
                }
            }
        }
    };

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            BluetoothWriteReadIntentService.LocalBinder binder = (BluetoothWriteReadIntentService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };

    public void readData(String name, int position)
    {
        if(mBound == true)
            mService.readData(name,position);
    }

    public void sendData(String name, int position)
    {
        if(mBound == true)
            mService.sendData(name,position);
    }


}