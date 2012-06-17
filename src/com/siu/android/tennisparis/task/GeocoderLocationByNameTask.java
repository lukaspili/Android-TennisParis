//package com.siu.android.tennisparis.task;
//
//import android.location.Address;
//import android.os.AsyncTask;
//import android.util.Log;
//import com.google.android.maps.GeoPoint;
//import com.siu.android.dondusang.Application;
//import com.siu.android.dondusang.activity.CenterMapActivity;
//import com.siu.android.dondusang.helper.LocationHelper;
//
//import java.io.IOException;
//import java.util.List;
//
///**
// * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
// */
//public class GeocoderLocationByNameTask extends AsyncTask<String, Void, GeoPoint> {
//
//    private CenterMapActivity activity;
//    private String name;
//
//    public GeocoderLocationByNameTask(CenterMapActivity activity) {
//        this.activity = activity;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        activity.setSupportProgressBarIndeterminateVisibility(true);
//    }
//
//    @Override
//    protected GeoPoint doInBackground(String... args) {
//        Log.d(getClass().getName(), "GeocoderLocationByNameTask");
//
//        name = args[0];
//
//        if (null == name) {
//            Log.w(getClass().getName(), "Location name to search is null");
//            return null;
//        }
//
//        List<Address> addresses;
//        try {
//            addresses = Application.getGeocoder().getFromLocationName(name, 10);
//        } catch (IOException e) {
//            Log.w(getClass().getName(), "Cannot get location addresses from geocoder", e);
//            return null;
//        }
//
//        if (addresses.isEmpty()) {
//            return null;
//        }
//
//        return LocationHelper.getGeoPoint(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
//    }
//
//    @Override
//    protected void onPostExecute(GeoPoint geoPoint) {
//        activity.setSupportProgressBarIndeterminateVisibility(false);
//
//        if (null == geoPoint) {
//            activity.onGeocoderFailure(name);
//        } else {
//            activity.onGeocoderSuccess(geoPoint);
//        }
//    }
//
//    @Override
//    protected void onCancelled(GeoPoint geoPoint) {
//        activity.setSupportProgressBarIndeterminateVisibility(false);
//    }
//
//    public void setActivity(CenterMapActivity activity) {
//        this.activity = activity;
//    }
//}
