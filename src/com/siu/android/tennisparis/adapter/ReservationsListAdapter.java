package com.siu.android.tennisparis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.model.Reservation;
import com.siu.android.tennisparis.util.DateUtils;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class ReservationsListAdapter extends ArrayAdapter<Reservation> {

    public ReservationsListAdapter(Context context, List<Reservation> reservations) {
        super(context, android.R.layout.simple_list_item_1, reservations);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (null == row) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.reservation_list_row, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        if (null == viewHolder) {
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }

        Reservation reservation = getItem(position);
        viewHolder.title.setText(reservation.getTitle());

        StringBuilder builder = new StringBuilder(DateUtils.formatAsFull(reservation.getDay()))
                .append(", de ")
                .append(reservation.getHour())
                .append("h à ")
                .append(reservation.getHour() + 1)
                .append("h");

        viewHolder.date.setText(builder.toString());

        return row;
    }

    public static class ViewHolder {
        private TextView title;
        private TextView date;

        public ViewHolder(View row) {
            title = (TextView) row.findViewById(R.id.reservation_list_row_title);
            date = (TextView) row.findViewById(R.id.reservation_list_row_date);
        }
    }

}
