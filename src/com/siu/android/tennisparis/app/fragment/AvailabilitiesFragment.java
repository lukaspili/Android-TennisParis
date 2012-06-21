package com.siu.android.tennisparis.app.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.adapter.AvailabilityListAdapter;
import com.siu.android.tennisparis.dao.model.Availability;
import com.siu.android.tennisparis.util.DateUtils;
import com.siu.android.tennisparis.util.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilitiesFragment extends Fragment {

    public static final String EXTRA_AVAILABILITIES = "extra_availabilities";

    private ListView listView;
    private TextView titleTextView;
    private AlertDialog reservationAlertDialog;

    private AvailabilityListAdapter availabilityListAdapter;
    private List<Availability> availabilities;
    private Availability selectedAvailability;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
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
        initList();
    }

    private void initList() {
        titleTextView.setText(DateUtils.formatAsFull(availabilities.get(0).getDay()));

        availabilityListAdapter = new AvailabilityListAdapter(getActivity(), availabilities);
        listView.setAdapter(availabilityListAdapter);
        listView.setDivider(null);
        listView.setDividerHeight(0);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (!Session.getInstance().isLogged()) {
                    Bundle args = new Bundle();
                    DialogFragment fragment = new LoginDialogFragment();
                    fragment.setArguments(args);

                    FragmentUtils.showDialog(getFragmentManager(), fragment);
                    return;
                }

                selectedAvailability = availabilityListAdapter.getItem(i);
                showDialog();
            }
        });
    }

    private void showDialog() {
        if (null == reservationAlertDialog) {
            reservationAlertDialog = new AlertDialog.Builder(getActivity())
                    .setTitle(R.string.reservation_dialog_title)
                    .setMessage(R.string.reservation_dialog_message)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .create();
        }

        reservationAlertDialog.show();
    }
}
