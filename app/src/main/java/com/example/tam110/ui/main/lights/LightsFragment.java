package com.example.tam110.ui.main.lights;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tam110.R;
import com.example.tam110.ui.main.lights.data.LightsData;
import com.example.tam110.ui.main.lights.data.LightsData.Light;

import java.util.Arrays;
import java.util.List;

import static com.example.tam110.ui.main.lights.data.LightsData.ITEMS_INITIALIZED;
import static com.example.tam110.ui.main.lights.data.LightsData.addLight;

/*final ToggleButton tableLightButton = (ToggleButton) root.findViewById(R.id.toggleButton21);
        final ToggleButton outsideLightButton = (ToggleButton) root.findViewById(R.id.roofMotorDOWN);


        final SeekBar tableLightSeekBar = (SeekBar) root.findViewById(R.id.tableLightSeekBar);
        final SeekBar outsideLightSeekBar = (SeekBar) root.findViewById(R.id.outsideLightSeekBar);

        tableLightSeekBar.setVisibility(View.GONE);
        outsideLightSeekBar.setVisibility(View.GONE);

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

        outsideLightButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //Toast.makeText(getActivity(), "UP prizgan", Toast.LENGTH_LONG).show();
                    outsideLightSeekBar.setVisibility(View.VISIBLE);
                } else {
                    outsideLightSeekBar.setVisibility(View.GONE);
                }
            }
        });*/
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.devices_fragment_list, container, false);

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
                    addLight(new LightsData.Light(names.get(i)));

                ITEMS_INITIALIZED = true;
            }

            recyclerView.setAdapter(new LightsRecyclerViewAdapter(LightsData.ITEMS, mListener));
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
