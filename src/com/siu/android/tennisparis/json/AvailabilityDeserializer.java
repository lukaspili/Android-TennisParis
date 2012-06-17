package com.siu.android.tennisparis.json;

import com.google.gson.*;
import com.siu.android.tennisparis.dao.model.Availability;

import java.lang.reflect.Type;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilityDeserializer implements JsonDeserializer<Availability> {

    private static final String TENNIS_ID = "tennis_id";
    private static final String DAY = "day";
    private static final String HOUR = "hour";
    private static final String GROUND = "ground";

    @Override
    public Availability deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();

        Availability availability = new Availability();
        availability.setWebserviceTennisId(CommonDeserializer.getLong(jsonObject.get(TENNIS_ID)));
        availability.setDay(CommonDeserializer.getDate(jsonObject.get(DAY)));
        availability.setHour(CommonDeserializer.getInteger(jsonObject.get(HOUR)));
        availability.setGround(CommonDeserializer.getString(jsonObject.get(GROUND)));

        return availability;
    }
}
