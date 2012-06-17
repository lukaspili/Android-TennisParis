package com.siu.android.tennisparis.map;

import com.google.android.maps.OverlayItem;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.util.LocationUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisOverlayItem extends OverlayItem {

    private Tennis tennis;

    public TennisOverlayItem(Tennis tennis) {
        super(LocationUtils.getGeoPoint(tennis.getLatitude(), tennis.getLongitude()), tennis.getName(),
                tennis.getFullAddress());
        this.tennis = tennis;
    }

    public Tennis getTennis() {
        return tennis;
    }
}