package com.siu.android.tennisparis.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.siu.android.tennisparis.app.fragment.AvailabilitiesFragment;
import com.siu.android.tennisparis.dao.model.Availability;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilitiesFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<List<Availability>> availabilities;

    public AvailabilitiesFragmentPagerAdapter(FragmentManager fm, List<List<Availability>> availabilities) {
        super(fm);
        this.availabilities = availabilities;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(AvailabilitiesFragment.EXTRA_AVAILABILITIES, (ArrayList<Availability>) availabilities.get(position));

        Fragment fragment = new AvailabilitiesFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public int getCount() {
        return availabilities.size();
    }
}
