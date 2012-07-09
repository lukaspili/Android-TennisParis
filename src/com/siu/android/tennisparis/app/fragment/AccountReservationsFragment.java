package com.siu.android.tennisparis.app.fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.siu.android.andutils.util.FragmentUtils;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.adapter.ReservationsListAdapter;
import com.siu.android.tennisparis.model.Reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AccountReservationsFragment extends Fragment {

    private ListView listView;
    private RelativeLayout currentReservationLayout;

    private ReservationsListAdapter reservationsListAdapter;
    private List<Reservation> reservations;
    private Reservation reservation;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        reservations = new ArrayList<Reservation>();
        reservations.add(new Reservation("Resa 1", new Date(), 8));
        reservations.add(new Reservation("Resa 2", new Date(), 9));
        reservations.add(new Reservation("Resa 3", new Date(), 10));
        reservations.add(new Reservation("Resa 4", new Date(), 11));
        reservation = new Reservation("Foobar", new Date(), 18);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.account_reservation_fragment, viewGroup, false);
        currentReservationLayout = (RelativeLayout) view.findViewById(R.id.account_reservation_current);
        listView = (ListView) view.findViewById(R.id.account_reservation_past_list);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initList();
        initLayouts();
    }

    private void initList() {
        reservationsListAdapter = new ReservationsListAdapter(getActivity(), reservations);
        listView.setAdapter(reservationsListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showReservationDialog(reservationsListAdapter.getItem(i));
            }
        });
    }

    private void initLayouts() {
        currentReservationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReservationDialog(reservation);
            }
        });
    }

    private void showReservationDialog(Reservation reservation) {
        Bundle args = new Bundle();
        args.putSerializable(ReservationDialogFragment.EXTRA_RESERVATION, reservation);

        DialogFragment fragment = new ReservationDialogFragment();
        fragment.setArguments(args);

        FragmentUtils.showDialog(getFragmentManager(), fragment);
    }

}
