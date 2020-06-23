package com.example.tam110.ui.main.devices.data;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.example.tam110.R;
import com.example.tam110.ui.main.Data;
import com.example.tam110.ui.main.devices.DevicesFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceData
{
    public static List<Device> ITEMS = new ArrayList<Device>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Device> ITEM_MAP = new HashMap<String, Device>();

    public static boolean ITEMS_INITIALIZED = false;

    public static void addDevice(Device item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.UUID, item);
    }

    public static class Device extends Data
    {
        public Device(String UUID, String name, boolean hasSensitivity, boolean checkBox, int UIposition, String fragment)
        {
            this.UUID = UUID;
            this.name = name;
            this.hasSensitivity = hasSensitivity;
            this.checkBox = checkBox;
            this.UIposition = UIposition;
            this.fragment = fragment;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
