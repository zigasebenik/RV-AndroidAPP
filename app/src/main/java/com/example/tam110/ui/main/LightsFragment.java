package com.example.tam110.ui.main;

import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.example.tam110.MainActivity;
import com.example.tam110.R;
import android.os.Build;
import android.view.Gravity;
import android.widget.ImageButton;
import android.view.ViewGroup.LayoutParams;
import android.widget.SeekBar;
import android.widget.ToggleButton;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;


public class LightsFragment extends Fragment
{

    private LightsViewModel mViewModel;

    public static LightsFragment newInstance()
    {
        return new LightsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        View root = inflater.inflate(R.layout.lights_fragment, container, false);


        final ToggleButton tableLightButton = (ToggleButton) root.findViewById(R.id.tableLightButton);
        final SeekBar tableLightSeekBar = (SeekBar) root.findViewById(R.id.tableLightSeekBar);
        tableLightSeekBar.setVisibility(View.GONE);

        tableLightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getActivity(), "UP prizgan", Toast.LENGTH_LONG).show();
                    tableLightSeekBar.setVisibility(View.VISIBLE);
                } else {
                    tableLightSeekBar.setVisibility(View.GONE);
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LightsViewModel.class);
        // TODO: Use the ViewModel
    }

}
