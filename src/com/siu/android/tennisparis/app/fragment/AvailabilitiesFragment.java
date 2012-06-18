package com.siu.android.tennisparis.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.adapter.AvailabilityListAdapter;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.util.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilitiesFragment extends Fragment {

    public static final String EXTRA_AVAILABILITIES = "extra_availabilities";

    private ListView listView;
    private TextView titleTextView;

    private AvailabilityListAdapter availabilityListAdapter;
    private List<Availability> availabilities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        availabilities = (ArrayList<Availability>) getArguments().get(EXTRA_AVAILABILITIES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.availabilities_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.availabilities_fragment_list);
        titleTextView = (TextView) view.findViewById(R.id.availabilities_fragment_title);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleTextView.setText(DateUtils.formatAsFull(availabilities.get(0).getDay()));

        availabilityListAdapter = new AvailabilityListAdapter(getActivity(), availabilities);
        listView.setAdapter(availabilityListAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
    }
}
