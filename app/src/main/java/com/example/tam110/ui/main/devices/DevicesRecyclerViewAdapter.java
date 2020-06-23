package com.example.tam110.ui.main.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.tam110.R;
import com.example.tam110.ui.main.devices.DevicesFragment.OnListFragmentInteractionListener;
import com.example.tam110.ui.main.devices.data.DeviceData.Device;

import java.util.Arrays;
import java.util.List;

public class DevicesRecyclerViewAdapter extends RecyclerView.Adapter<DevicesRecyclerViewAdapter.ViewHolder>
{
    private final List<Device> mDevices;

    private final OnListFragmentInteractionListener mListener;

    public DevicesRecyclerViewAdapter(List<Device> listOfDeviceData, OnListFragmentInteractionListener listener)
    {
        mDevices = listOfDeviceData;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.devices_fragment_normal, parent, false);

        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, int position)
    {

        holder.mItem = mDevices.get(position);
        holder.mNameView.setText(mDevices.get(position).name);
        holder.mToggleButtonView.setChecked(mDevices.get(position).checkBox);

        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (null != mListener)
                {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });

        holder.mToggleButtonView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                holder.mItem.checkBox = isChecked;
            }
        });

    }


    @Override
    public int getItemCount()
    {
        return mDevices.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public final View mView;
        public final TextView mNameView;
        public final ToggleButton mToggleButtonView;
        public Device mItem;

        public ViewHolder(View view)
        {
            super(view);
            mView = view;
            mNameView = (TextView) view.findViewById(R.id.deviceName);
            mToggleButtonView = (ToggleButton) view.findViewById(R.id.toggleDevice);
        }

        @Override
        public String toString()
        {
            return super.toString() + " '" + mNameView.getText() + "'";
        }

    }

}
