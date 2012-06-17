package com.siu.android.tennisparis.json;

import android.util.Log;
import com.google.gson.*;
import com.siu.android.tennisparis.dao.model.Tennis;

import java.lang.reflect.Type;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisDeserializer implements JsonDeserializer<Tennis> {

    private static final String WEBSERVICE_ID = "id";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String POSTAL_CODE = "zip_code";
    private static final String CITY = "ville";
    private static final String ACTIVE = "is_active";

    @Override
    public Tennis deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Tennis tennis = new Tennis();
        tennis.setWebserviceId(CommonDeserializer.getLong(jsonObject.get(WEBSERVICE_ID)));
        tennis.setLatitude(CommonDeserializer.getDouble(jsonObject.get(LATITUDE)));
        tennis.setLongitude(CommonDeserializer.getDouble(jsonObject.get(LONGITUDE)));
        tennis.setName(CommonDeserializer.getString(jsonObject.get(NAME)));
        tennis.setAddress(CommonDeserializer.getString(jsonObject.get(ADDRESS)));
        tennis.setPostalCode(CommonDeserializer.getString(jsonObject.get(POSTAL_CODE)));
        tennis.setCity(CommonDeserializer.getString(jsonObject.get(CITY)));
        tennis.setActive(CommonDeserializer.getBoolean(jsonObject.get(ACTIVE)));

        return tennis;
    }
}
