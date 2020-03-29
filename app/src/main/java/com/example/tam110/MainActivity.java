package com.example.tam110;

import android.os.Bundle;

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


        BluetoothConnection.setupConnection(this);

    }

    @Override
    public void onListFragmentInteraction(DeviceData.Device item)
    {

    }

    @Override
    public void onListFragmentInteraction(LightsData.Light item)
    {

    }
}