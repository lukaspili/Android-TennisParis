package com.siu.android.tennisparis.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.dao.model.Tennis;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class GsonFormatter {

    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(Tennis.class, new TennisDeserializer())
            .registerTypeAdapter(Availability.class, new AvailabilityDeserializer())
            .create();

    public static Gson getGson() {
        return gson;
    }
}
