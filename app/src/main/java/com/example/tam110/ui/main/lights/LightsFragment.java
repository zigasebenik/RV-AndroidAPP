package com.example.tam110.ui.main.lights;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tam110.MainActivity;
import com.example.tam110.R;
import com.example.tam110.communication.bluetooth.BluetoothWriteReadIntentService;
import com.example.tam110.ui.main.lights.data.LightsData;
import com.example.tam110.ui.main.lights.data.LightsData.Light;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.example.tam110.ui.main.lights.data.LightsData.ITEMS_INITIALIZED;
import static com.example.tam110.ui.main.lights.data.LightsData.addLight;

public class LightsFragment extends Fragment
{

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    public static final String UPDATE_LIGHTS_UI = "UPDATE_LIGHTS_UI";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    LightsRecyclerViewAdapter viewAdapter;
    MainActivity mainActivity;
    final Handler handler = new Handler();

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LightsFragment()
    {
    }


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

        mainActivity = (MainActivity) getActivity();
    }

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
                List<String> arrayOfJson = Arrays.asList(this.getResources().getStringArray(R.array.Lights));


                for(int i=0;i<arrayOfJson.size(); i++)
                {
                    JSONObject jsonObject = null;
                    try
                    {
                        jsonObject = new JSONObject(arrayOfJson.get(i).replaceAll("\\s", "|"));
                        String name = jsonObject.get("name").toString().replaceAll("\\|", " ");
                        String UUID = jsonObject.get("UUID").toString();
                        boolean analog = jsonObject.getBoolean("analog");

                        addLight(new LightsData.Light(UUID, name, analog, false, i, LightsFragment.class.toString()));
                        //handler.postDelayed(new ReadCharacteristic(UUID), 500*i);

                    } catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
                //mainActivity.updateLightsUI();

                ITEMS_INITIALIZED = true;
            }

            viewAdapter = new LightsRecyclerViewAdapter(LightsData.ITEMS, mListener, mainActivity);
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
        LocalBroadcastManager.getInstance(mainActivity).registerReceiver(receiver, new IntentFilter(UPDATE_LIGHTS_UI));
    }
    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(mainActivity).unregisterReceiver(receiver);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String value = intent.getStringExtra(BluetoothWriteReadIntentService.DATA);
            int position = intent.getIntExtra(BluetoothWriteReadIntentService.DEVICE_POSITION, -1);

            int sensitivity = stringToInt(value);

            if(value.equals("ON"))
            {
                LightsData.ITEMS.get(position).checkBox = true;
                viewAdapter.notifyItemChanged(position);
            }
            else if(value.equals("OFF"))
            {
                LightsData.ITEMS.get(position).checkBox = false;
                viewAdapter.notifyItemChanged(position);
            }
            else if(sensitivity > 0 && sensitivity != -1)
            {
                LightsData.ITEMS.get(position).checkBox = true;
                if(sensitivity == 255)
                    viewAdapter.notifyItemChanged(position);

                if(LightsData.ITEMS.get(position).init == false)
                {
                    LightsData.ITEMS.get(position).init = true;
                    viewAdapter.notifyItemChanged(position);
                }
            }


        }
    };

    public static int stringToInt(String param) {
        try {
            return Integer.valueOf(param);
        } catch(NumberFormatException e) {
            return -1;
        }
    }

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


    public class ReadCharacteristic implements Runnable {
        private String UUID;
        public ReadCharacteristic(String UUID) {
            this.UUID = UUID;
        }

        @Override
        public void run()
        {
            mainActivity.runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    mainActivity.readData(UUID);
                }
            });
        }
    }

}
