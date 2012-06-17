package com.siu.android.tennisparis.app.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.MenuItem;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.adapter.AvailabilitiesFragmentPagerAdapter;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.dao.model.AvailabilityDao;
import com.siu.android.tennisparis.dao.model.Tennis;
import com.siu.android.tennisparis.database.DatabaseHelper;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.PageIndicator;

import java.util.*;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisDetailActivity extends SherlockFragmentActivity {

    public static final String EXTRA_TENNIS = "extra_tennis";

    private TextView nameTextView;
    private TextView addressTextView;
    private ViewPager viewPager;
    private PageIndicator pageIndicator;

    private AvailabilitiesFragmentPagerAdapter tennisCourtFragmentAdapter;
    private Tennis tennis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tennis_detail_activity);

        nameTextView = (TextView) findViewById(R.id.tennis_detail_activity_name);
        addressTextView = (TextView) findViewById(R.id.tennis_detail_activity_address);
        viewPager = (ViewPager) findViewById(R.id.tennis_detail_activity_pager);
        pageIndicator = (CirclePageIndicator) findViewById(R.id.tennis_detail_activity_indicator);

        initActionBar();

        if (null != savedInstanceState) {
            tennis = (Tennis) savedInstanceState.get(EXTRA_TENNIS);
        } else {
            tennis = (Tennis) getIntent().getExtras().get(EXTRA_TENNIS);
        }

        initTennis();
        initViewPager();
    }

    private void initActionBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initTennis() {
        nameTextView.setText(tennis.getName());
        addressTextView.setText(tennis.getFullAddress());
    }

    private void initViewPager() {
        List<Availability> availabilities = DatabaseHelper.getInstance().getDaoSession().getAvailabilityDao()
                .queryBuilder()
                .where(AvailabilityDao.Properties.WebserviceTennisId.eq(tennis.getWebserviceId()))
                .orderAsc(AvailabilityDao.Properties.Day)
                .list();

        List<List<Availability>> filteredAvailabilities = new ArrayList<List<Availability>>();

        if (!availabilities.isEmpty()) {
            filteredAvailabilities.add(new ArrayList<Availability>());

            for (Iterator<Availability> it = availabilities.iterator(); it.hasNext(); ) {
                Availability availability = it.next();
                List<Availability> currentList = filteredAvailabilities.get(filteredAvailabilities.size() - 1);

                if (currentList.isEmpty()) {
                    currentList.add(availability);
                } else {
                    if (currentList.get(currentList.size() - 1).getDay().equals(availability.getDay())) {
                        currentList.add(availability);
                    } else {
                        currentList = new ArrayList<Availability>();
                        currentList.add(availability);
                        filteredAvailabilities.add(currentList);
                    }
                }
            }
        }

        tennisCourtFragmentAdapter = new AvailabilitiesFragmentPagerAdapter(getSupportFragmentManager(), filteredAvailabilities);
        viewPager.setAdapter(tennisCourtFragmentAdapter);
        pageIndicator.setViewPager(viewPager);
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
    }
}
