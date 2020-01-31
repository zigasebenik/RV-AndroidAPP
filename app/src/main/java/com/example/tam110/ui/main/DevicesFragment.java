package com.example.tam110.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.tam110.R;

public class DevicesFragment extends Fragment
{

    private DevicesViewModel mViewModel;

    public static DevicesFragment newInstance()
    {
        return new DevicesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final View root = inflater.inflate(R.layout.devices_fragment, container, false);
        final ToggleButton roofMotorUP = (ToggleButton) root.findViewById(R.id.roofMotorUP);
        final ToggleButton roofMotorDOWN = (ToggleButton) root.findViewById(R.id.roofMotorDOWN);

        roofMotorUP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getActivity(), "UP prizgan", Toast.LENGTH_LONG).show();
                    roofMotorDOWN.setVisibility(View.INVISIBLE);
                } else {
                    roofMotorDOWN.setVisibility(View.VISIBLE);
                }
            }
        });

        roofMotorDOWN.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getActivity(), "DOWN prizgan", Toast.LENGTH_LONG).show();
                    roofMotorUP.setVisibility(View.INVISIBLE);
                } else {
                    roofMotorUP.setVisibility(View.VISIBLE);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(DevicesViewModel.class);
        // TODO: Use the ViewModel
    }

}
