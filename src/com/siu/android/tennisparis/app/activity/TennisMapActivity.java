package com.siu.android.tennisparis.app.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockMapActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.apps.analytics.easytracking.EasyTracker;
import com.google.android.maps.GeoPoint;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.adapter.TennisListAdapter;
import com.siu.android.tennisparis.map.EnhancedMapView;
import com.siu.android.tennisparis.map.TennisOverlay;
import com.siu.android.tennisparis.task.CurrentLocationTask;
import com.siu.android.tennisparis.task.TennisLoadTask;
import com.siu.android.tennisparis.task.TennisUpdateTask;
import com.siu.android.tennisparis.toast.AppToast;
import com.siu.android.tennisparis.util.LocationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
@SuppressWarnings("deprecation")
public class TennisMapActivity extends SherlockMapActivity {

    private EnhancedMapView mapView;
    private ListView listView;
    private TennisListAdapter listAdapter;

    private Runnable tennisUpdateRunnable;
    private Handler tennisUpdateHandler = new Handler();

    private TennisUpdateTask tennisUpdateTask;
    private CurrentLocationTask currentLocationTask;
    private TennisLoadTask tennisLoadTask;

    private TennisOverlay tennisOverlay;

    private List<Tennis> tennises = new ArrayList<Tennis>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.tennis_map_activity);

        setSupportProgressBarIndeterminateVisibility(false);

        mapView = (EnhancedMapView) findViewById(R.id.map);
        listView = (ListView) findViewById(android.R.id.list);

        initActionBar();

        RetainInstance retainInstance = (RetainInstance) getLastNonConfigurationInstance();
        if (null != retainInstance) {
            tennises = (ArrayList<Tennis>) savedInstanceState.getSerializable("tennises");
            getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab"));

            currentLocationTask = retainInstance.currentLocationTask;
            currentLocationTask.setActivity(this);
            if (currentLocationTask.isRunning()) {
                setSupportProgressBarIndeterminateVisibility(true);
            }

            if (null != retainInstance.tennisLoadTask) {
                tennisLoadTask = retainInstance.tennisLoadTask;
                tennisLoadTask.setActivity(this);
                setSupportProgressBarIndeterminateVisibility(true);
            }

            if (null != retainInstance.tennisUpdateTask) {
                tennisUpdateTask = retainInstance.tennisUpdateTask;
                tennisUpdateTask.setActivity(this);
            }

        } else {
            tennises = new ArrayList<Tennis>();

            startTennisUpdate();
            startTennisLoading();

            currentLocationTask = new CurrentLocationTask(this);
            mapView.getController().setZoom(13);
            mapView.getController().setCenter(LocationUtils.getParisGeoPoint());
        }

        initMap();
        initList();

        EasyTracker.getTracker().setContext(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        EasyTracker.getTracker().trackActivityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EasyTracker.getTracker().trackActivityStop(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (isFinishing()) {
            currentLocationTask.stopCurrentLocation();
            stopTennisLoadingIfRunning();
            stopTennisUpdateIfRunning();
        }
    }

    @Override
    public Object onRetainNonConfigurationInstance() {
        RetainInstance retainInstance = new RetainInstance();

        currentLocationTask.setActivity(null);
        retainInstance.currentLocationTask = currentLocationTask;

        if (null != tennisLoadTask) {
            tennisLoadTask.setActivity(null);
            retainInstance.tennisLoadTask = tennisLoadTask;
        }

        if (null != tennisUpdateTask) {
            tennisUpdateTask.setActivity(null);
            retainInstance.tennisUpdateTask = tennisUpdateTask;
        }

        EasyTracker.getTracker().trackActivityRetainNonConfigurationInstance();
        return retainInstance;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("tennises", (ArrayList<Tennis>) tennises);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.tennis_map_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_location:
                stopTennisLoadingIfRunning();
                currentLocationTask.startCurrentLocation();
                setSupportProgressBarIndeterminateVisibility(true);
                break;

            case R.id.menu_login:
//                startActivity(new Intent(this, InfosActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    /* Tennis update */

    private void startTennisUpdate() {
        stopTennisUpdateIfRunning();
        tennisUpdateRunnable = new Runnable() {
            @Override
            public void run() {
                tennisUpdateTask = new TennisUpdateTask(TennisMapActivity.this);
                tennisUpdateTask.execute();
            }
        };

        tennisUpdateHandler.post(tennisUpdateRunnable);
    }

    private void stopTennisUpdateIfRunning() {
        if (null == tennisUpdateTask) {
            return;
        }

        tennisUpdateTask.cancel(true);
        tennisUpdateTask = null;
    }

    public void onTennisUpdateTaskProgress() {
        startTennisLoading();
    }

    public void onTennisUpdateTaskFinish(boolean progressCalled) {
        if (!progressCalled) {
            startTennisLoading();
        }

        tennisUpdateTask = null;
        tennisUpdateHandler.postDelayed(tennisUpdateRunnable, 1000 * 60 * 30);
    }

//    private void initAndBindService() {
//        tennisUpdaterServiceConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//            }
//        };
//
//        tennisUpdaterBroadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                Log.d(getClass().getName(), "TennisUpdaterBroadcastReceiver");
//                if (tennises.isEmpty()) {
//                    startTennisLoading();
//                }
//            }
//        };
//
//        registerReceiver(tennisUpdaterBroadcastReceiver, new IntentFilter(Intent.ACTION_EDIT));
//        Application.getContext().bindService(new Intent(this, TennisUpdaterService.class), tennisUpdaterServiceConnection, BIND_AUTO_CREATE);
//    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab();
        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_earth));
        tab.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                mapView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                mapView.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_list));
        tab.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                listView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tab);

        tab = actionBar.newTab();
        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_favorites));
        tab.setTabListener(new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        });
        actionBar.addTab(tab);
    }

    private void initMap() {
        mapView.setBuiltInZoomControls(true);
        mapView.setOnChangeListener(new EnhancedMapView.OnChangeListener() {
            @Override
            public void onChange(EnhancedMapView view, final GeoPoint newCenter, GeoPoint oldCenter, int newZoom, int oldZoom) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentLocationTask.stopCurrentLocation();
                        setSupportProgressBarIndeterminateVisibility(false);
                    }
                });
            }
        });

        tennisOverlay = new TennisOverlay(getResources().getDrawable(R.drawable.tennis_marker), mapView);

        if (!tennises.isEmpty()) {
            tennisOverlay.addTennises(tennises);
        }

        mapView.getOverlays().add(tennisOverlay);
    }

    private void initList() {
        listAdapter = new TennisListAdapter(this, tennises);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TennisMapActivity.this, TennisDetailActivity.class);
                intent.putExtra(TennisDetailActivity.EXTRA_TENNIS, listAdapter.getItem(i));
                startActivity(intent);
            }
        });
    }


    /* Current Location */

    public void onCurrentLocationSuccess(Location location) {
        locatePositionOnMap(LocationUtils.getGeoPoint(location));
    }

    public void onCurrentLocationFailure() {
        new AppToast(TennisMapActivity.this, R.string.tennis_map_error_current_location).show();
    }


    /* Tennis Location */

    private void startTennisLoading() {
        stopTennisLoadingIfRunning();
        setSupportProgressBarIndeterminateVisibility(true);

        tennisLoadTask = new TennisLoadTask(this);
        tennisLoadTask.execute();
    }

    private void stopTennisLoadingIfRunning() {
        if (null == tennisLoadTask) {
            return;
        }

        tennisLoadTask.cancel(true);
        tennisLoadTask = null;

        setSupportProgressBarIndeterminateVisibility(false);
    }

    public void onTennisLoadTaskFinish(List<Tennis> receivedTennis) {
        setSupportProgressBarIndeterminateVisibility(false);
        tennisLoadTask = null;

        Log.d(getClass().getName(), "Tennises : " + receivedTennis.size());

        if (null == receivedTennis || receivedTennis.isEmpty()) {
            return;
        }
        tennises.clear();
        tennises.addAll(receivedTennis);

        tennisOverlay.getOverlayItems().clear();
        tennisOverlay.addTennises(tennises);

        listAdapter.notifyDataSetChanged();
        mapView.invalidate();
    }

    private void locatePositionOnMap(GeoPoint geoPoint) {
        mapView.getController().animateTo(geoPoint);
    }

    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }

    private static class RetainInstance {
        private CurrentLocationTask currentLocationTask;
        private TennisLoadTask tennisLoadTask;
        private TennisUpdateTask tennisUpdateTask;
    }
}