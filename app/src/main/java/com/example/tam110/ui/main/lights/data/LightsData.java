package com.example.tam110.ui.main.lights.data;

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
        ITEM_MAP.put(item.name, item);
    }

    public static class Light
    {
        public final String name;
        public boolean checkbox;

        public Light(String name)
        {
            this.name = name;
            checkbox = false;
        }

        @Override
        public String toString()
        {
            return name;
        }
    }
}
