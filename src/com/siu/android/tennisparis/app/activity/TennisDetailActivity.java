package com.siu.android.tennisparis.app.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.Window;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.adapter.AvailabilitiesFragmentPagerAdapter;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.task.AvailabilityLoadTask;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisDetailActivity extends SherlockFragmentActivity {

    public static final String EXTRA_TENNIS = "extra_tennis";
    public static final String EXTRA_AVAILABILITIES = "extra_availabilities";

    private TextView nameTextView;
    private TextView addressTextView;
    private LinearLayout viewPagerLinearLayout;
    private ScrollView emptyScrollView;
    private ViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;

    private AvailabilityLoadTask availabilityLoadTask;

    private AvailabilitiesFragmentPagerAdapter tennisCourtFragmentAdapter;
    private List<List<Availability>> availabilitiesPerDay;
    private Tennis tennis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.tennis_detail_activity);

        setSupportProgressBarIndeterminateVisibility(false);

        nameTextView = (TextView) findViewById(R.id.tennis_detail_activity_name);
        addressTextView = (TextView) findViewById(R.id.tennis_detail_activity_address);
        viewPagerLinearLayout = (LinearLayout) findViewById(R.id.tennis_detail_activity_viewpager_layout);
        emptyScrollView = (ScrollView) findViewById(R.id.tennis_detail_activity_empty);
        viewPager = (ViewPager) findViewById(R.id.tennis_detail_activity_pager);
        circlePageIndicator = (CirclePageIndicator) findViewById(R.id.tennis_detail_activity_indicator);

        initActionBar();

        if (null != savedInstanceState) {
            tennis = (Tennis) savedInstanceState.get(EXTRA_TENNIS);
            availabilitiesPerDay = (ArrayList<List<Availability>>) savedInstanceState.get(EXTRA_AVAILABILITIES);
        } else {
            tennis = (Tennis) getIntent().getExtras().get(EXTRA_TENNIS);
            availabilitiesPerDay = new ArrayList<List<Availability>>();
            loadAvailabilities();
        }

        availabilityLoadTask = (AvailabilityLoadTask) getLastCustomNonConfigurationInstance();
        if (null != availabilityLoadTask) {
            availabilityLoadTask.setActivity(this);
            setSupportProgressBarIndeterminateVisibility(true);
        }

        initTennis();
        initViewPager();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (availabilitiesPerDay.isEmpty() && null == availabilityLoadTask) {
            loadAvailabilities();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_TENNIS, tennis);
        outState.putSerializable(EXTRA_AVAILABILITIES, (ArrayList<List<Availability>>) availabilitiesPerDay);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        if (null != availabilityLoadTask) {
            availabilityLoadTask.setActivity(null);
        }

        return availabilityLoadTask;
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initTennis() {
        nameTextView.setText(tennis.getName());
        addressTextView.setText(tennis.getFullAddress());
    }

    private void initViewPager() {
        tennisCourtFragmentAdapter = new AvailabilitiesFragmentPagerAdapter(getSupportFragmentManager(), availabilitiesPerDay);
        viewPager.setAdapter(tennisCourtFragmentAdapter);
        circlePageIndicator.setViewPager(viewPager);

        final float density = getResources().getDisplayMetrics().density;
        circlePageIndicator.setRadius(4 * density);
        circlePageIndicator.setPageColor(0xFFCCCCCC);
        circlePageIndicator.setFillColor(0xFF000000);
        circlePageIndicator.setStrokeColor(0xFF000000);
        circlePageIndicator.setStrokeWidth(1 * density);
    }

    private void loadAvailabilities() {
        setSupportProgressBarIndeterminateVisibility(true);
        availabilityLoadTask = new AvailabilityLoadTask(this, tennis);
        availabilityLoadTask.execute();
    }

    public void onAvailabilityLoadTaskFinish(List<List<Availability>> loadedAvailabilitiesPerDay) {
        setSupportProgressBarIndeterminateVisibility(false);
        availabilityLoadTask = null;

        if (loadedAvailabilitiesPerDay.isEmpty()) {
            viewPagerLinearLayout.setVisibility(View.GONE);
            emptyScrollView.setVisibility(View.VISIBLE);
            return;
        }

        availabilitiesPerDay.clear();
        availabilitiesPerDay.addAll(loadedAvailabilitiesPerDay);

        tennisCourtFragmentAdapter.notifyDataSetChanged();
        circlePageIndicator.notifyDataSetChanged();
    }
}