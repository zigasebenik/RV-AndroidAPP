package com.example.tam110.ui.main.lights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tam110.R;
import com.example.tam110.communication.bluetooth.BluetoothWriteReadIntentService;
import com.example.tam110.ui.main.lights.data.LightsData;
import com.example.tam110.ui.main.lights.data.LightsData.Light;

import java.util.Arrays;
import java.util.List;

import static com.example.tam110.ui.main.lights.data.LightsData.ITEMS_INITIALIZED;
import static com.example.tam110.ui.main.lights.data.LightsData.addLight;

public class LightsFragment extends Fragment
{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightsFragment()
    {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static LightsFragment newInstance(int columnCount)
    {
        LightsFragment fragment = new LightsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        if (getArguments() != null)
        {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    LightsRecyclerViewAdapter viewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.lights_fragment_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView)
        {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1)
            {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            }
            else
            {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            if(ITEMS_INITIALIZED == false)
            {
                List<String> names = Arrays.asList(this.getResources().getStringArray(R.array.Lights));


                for(int i=0;i<names.size(); i++)
                {
                    String name = names.get(i);

                    Intent getDataFromServer = new Intent(getContext(), BluetoothWriteReadIntentService.class);
                    getDataFromServer.putExtra(BluetoothWriteReadIntentService.DEVICE_POSITION, i);
                    getDataFromServer.putExtra(BluetoothWriteReadIntentService.DEVICE_NAME, name);
                    getDataFromServer.setAction(BluetoothWriteReadIntentService.READ_DATA);
                    getContext().startService(getDataFromServer);

                    if(i < 2)
                        addLight(new LightsData.Light(name, true, false));
                    else
                        addLight(new LightsData.Light(name, false, false));
                }

                ITEMS_INITIALIZED = true;
            }

            viewAdapter = new LightsRecyclerViewAdapter(LightsData.ITEMS, mListener);
            recyclerView.setAdapter(viewAdapter);

        }

        return view;
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener)
        {
            mListener = (OnListFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                                               + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(receiver, new IntentFilter("UpdateMessageIntent"));
    }
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra(BluetoothWriteReadIntentService.READ_DATA);
            int position = intent.getIntExtra(BluetoothWriteReadIntentService.DEVICE_POSITION, -1);

            if(value.equals("ON"))
                LightsData.ITEMS.get(position).checkBox = true;
            else if(value.equals("OFF"))
                LightsData.ITEMS.get(position).checkBox = false;


            viewAdapter.notifyItemChanged(position);
        }
    };

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnListFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Light item);
    }

}
