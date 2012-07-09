package com.siu.android.tennisparis.task;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.siu.android.tennisparis.Application;
import com.siu.android.tennisparis.app.activity.TennisMapActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author Lukasz Piliszczuk <lukasz.piliszczuk AT zenika.com>
 */
public class CurrentLocationTask {

    private TennisMapActivity activity;
    private LocationManager locationManager;
    private LocationListener locationListener = new LocationListener();
    private Listener lastLocationHandler = new Listener();
    private Timer getLastLocationTaskTimer;
    private boolean running;

    public CurrentLocationTask(TennisMapActivity activity) {
        this.activity = activity;
        locationManager = (LocationManager) Application.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    public void startCurrentLocation() {
        Log.d(getClass().getName(), "CurrentLocationTask");
        running = true;

        if (isGpsEnabled()) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        } else if (isNetworkEnabled()) {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        } else {
            Log.w(getClass().getName(), "Gps and network are disabled, cannot get current location");
            stopCurrentLocation();
            return;
        }

        if (null != getLastLocationTaskTimer) {
            getLastLocationTaskTimer.cancel();
        }

        getLastLocationTaskTimer = new Timer();
        getLastLocationTaskTimer.schedule(new GetLastLocationTask(), 15000);
    }

    public void stopCurrentLocation() {
        running = false;

        if (null != getLastLocationTaskTimer) {
            getLastLocationTaskTimer.cancel();
        }

        locationManager.removeUpdates(locationListener);
    }

    private boolean isGpsEnabled() {
        try {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception e) {
            Log.w(getClass().getSimpleName(), "Gps provider is not enabled", e);
        }

        return false;
    }

    private boolean isNetworkEnabled() {
        try {
            return locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception e) {
            Log.w(getClass().getSimpleName(), "Network provider is not enabled", e);
        }

        return false;
    }

    private class Listener extends Handler {
        @Override
        public void handleMessage(Message message) {
            stopCurrentLocation();

            if (null != message.obj) {
                activity.onCurrentLocationSuccess((Location) message.obj);
            } else {
                activity.onCurrentLocationFailure();
            }
        }
    }

    private class GetLastLocationTask extends TimerTask {
        @Override
        public void run() {
            Location networkLocation = null, gpsLocation = null, bestLocation = null;

            if (isGpsEnabled()) {
                gpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }

            if (isNetworkEnabled()) {
                networkLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (gpsLocation != null && networkLocation != null) {

                if (gpsLocation.getTime() > networkLocation.getTime()) {
                    bestLocation = gpsLocation;
                } else {
                    bestLocation = networkLocation;
                }

            } else if (gpsLocation != null) {
                bestLocation = gpsLocation;

            } else if (networkLocation != null) {
                bestLocation = networkLocation;
            }

            Message message = lastLocationHandler.obtainMessage();
            message.obj = bestLocation;
            message.sendToTarget();
        }
    }

    private class LocationListener implements android.location.LocationListener {

        @Override
        public void onLocationChanged(Location location) {
            activity.onCurrentLocationSuccess(location);
            stopCurrentLocation();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {
        }

        @Override
        public void onProviderEnabled(String s) {
        }

        @Override
        public void onProviderDisabled(String s) {
        }
    }

    public void setActivity(TennisMapActivity activity) {
        this.activity = activity;
    }

    public boolean isRunning() {
        return running;
    }
}