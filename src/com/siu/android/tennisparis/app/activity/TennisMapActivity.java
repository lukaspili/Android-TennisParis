package com.siu.android.tennisparis.app.activity;

import android.app.Dialog;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.RHTxjGZv.MHxPKJqg108715.Airpush;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.google.android.apps.analytics.easytracking.EasyTracker;
import com.google.android.maps.GeoPoint;
import com.siu.android.andgapisutils.activity.tracked.TrackedSherlockMapActivity;
import com.siu.android.andgapisutils.util.LocationUtils;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.adapter.TennisListAdapter;
import com.siu.android.tennisparis.app.dialog.LoginDialog;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.map.EnhancedMapView;
import com.siu.android.tennisparis.map.TennisOverlay;
import com.siu.android.tennisparis.task.CurrentLocationTask;
import com.siu.android.tennisparis.task.TennisLoadTask;
import com.siu.android.tennisparis.toast.AppToast;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
@SuppressWarnings("deprecation")
public class TennisMapActivity extends TrackedSherlockMapActivity {

    private EnhancedMapView mapView;
    private ListView listView;
    private TennisListAdapter listAdapter;

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

        } else {
            tennises = new ArrayList<Tennis>();

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
    protected void onResume() {
        super.onResume();


        if (tennises.isEmpty() && null == tennisLoadTask) {
            startTennisLoading();
        }
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
                currentLocationTask.startCurrentLocation();
                setSupportProgressBarIndeterminateVisibility(true);
                break;

            case R.id.menu_login:
                if (!Session.getInstance().isLogged()) {
                    showDialog(1);
                    return true;
                }

                startActivity(new Intent(this, AccountActivity.class));
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        return new LoginDialog(this);
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setHomeButtonEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        ActionBar.Tab tab = actionBar.newTab();
        tab.setIcon(getResources().getDrawable(R.drawable.ic_menu_map));
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

        if (Session.getInstance().isLogged()) {
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

        tennisOverlay = new TennisOverlay(getResources().getDrawable(R.drawable.ic_marker), mapView);

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
        setSupportProgressBarIndeterminateVisibility(false);
        locatePositionOnMap(LocationUtils.getGeoPoint(location));
    }

    public void onCurrentLocationFailure() {
        setSupportProgressBarIndeterminateVisibility(false);
        new AppToast(TennisMapActivity.this, R.string.tennis_map_error_current_location).show();
    }


    /* Tennis Location */

    private void startTennisLoading() {
        setSupportProgressBarIndeterminateVisibility(true);
        tennisLoadTask = new TennisLoadTask(this);
        tennisLoadTask.execute();
    }

    public void onTennisLoadTaskFinish(List<Tennis> receivedTennis) {
        setSupportProgressBarIndeterminateVisibility(false);
        tennisLoadTask = null;

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
    }

}