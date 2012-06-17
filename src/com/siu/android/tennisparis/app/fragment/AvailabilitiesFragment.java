package com.siu.android.tennisparis.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.dao.model.Availability;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilitiesFragment extends Fragment {

    public static final String EXTRA_AVAILABILITIES = "extra_availabilities";

    private ListView listView;
    private TextView titleTextView;

    private ArrayAdapter<Availability> adapter;
    private List<Availability> availabilities;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        availabilities = (ArrayList<Availability>) getArguments().get(EXTRA_AVAILABILITIES);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tennis_court_fragment, container, false);
        listView = (ListView) view.findViewById(R.id.tennis_court_fragment_list);
        titleTextView = (TextView) view.findViewById(R.id.tennis_court_fragment_title);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        titleTextView.setText(availabilities.get(0).getDay().toString());

        adapter = new ArrayAdapter<Availability>(getActivity(), android.R.layout.simple_list_item_1, availabilities);
        listView.setAdapter(adapter);
    }
}
