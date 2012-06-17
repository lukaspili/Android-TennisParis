package com.siu.android.tennisparis;

import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class DaoGenerator {

    public static void main(String[] args) throws Exception {

        Schema schema = new Schema(1, "com.siu.android.tennisparis.dao.model");
        schema.enableKeepSectionsByDefault();

        Entity availability = schema.addEntity("Availability");
        availability.implementsSerializable();
        availability.addIdProperty();
        availability.addDateProperty("day");
        availability.addIntProperty("hour");
        availability.addStringProperty("ground");
        availability.addLongProperty("webserviceTennisId");

        Entity tennis = schema.addEntity("Tennis");
        tennis.implementsSerializable();
        tennis.addIdProperty();
        tennis.addLongProperty("webserviceId");
        tennis.addStringProperty("name");
        tennis.addStringProperty("address");
        tennis.addStringProperty("postalCode");
        tennis.addStringProperty("city");
        tennis.addDoubleProperty("latitude");
        tennis.addDoubleProperty("longitude");
        tennis.addBooleanProperty("active");

//        ToMany tennisToAvailabilities = tennis.addToMany(availability, tennisId);
//        tennisToAvailabilities.setName("entries");

        new de.greenrobot.daogenerator.DaoGenerator().generateAll(schema, "./src-gen");
    }
}
