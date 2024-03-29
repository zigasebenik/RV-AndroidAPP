package com.example.tam110.ui.main.lights.data;

import com.example.tam110.ui.main.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LightsData
{
    public static List<Light> ITEMS = new ArrayList<Light>();
    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, Light> ITEM_MAP = new HashMap<String, Light>();

    public static boolean ITEMS_INITIALIZED = false;

    public static void addLight(Light item)
    {
        ITEMS.add(item);
        ITEM_MAP.put(item.UUID, item);
    }

    public static class Light extends Data
    {
        public Light(String UUID, String name, boolean hasSensitivity, boolean checkBox, int UIposition, String fragment)
        {
            this.UUID = UUID;
            this.name = name;
            this.hasSensitivity = hasSensitivity;
            this.checkBox = checkBox;
            this.UIposition = UIposition;
            this.fragment = fragment;
            this.init = false;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
