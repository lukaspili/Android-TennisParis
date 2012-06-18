package com.siu.android.tennisparis.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.dao.model.Availability;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AvailabilityListAdapter extends ArrayAdapter<Availability> {

    public AvailabilityListAdapter(Context context, List<Availability> availabilities) {
        super(context, R.layout.availability_list_row, availabilities);
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        if (null == row) {
            row = LayoutInflater.from(getContext()).inflate(R.layout.availability_list_row, null, false);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        if (null == viewHolder) {
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }

        Availability availability = getItem(position);

        String hour = String.format(getContext().getString(R.string.availability_hour_range), availability.getHour(), availability.getHour() + 1);
        viewHolder.hourTextView.setText(hour);
        viewHolder.courtTextView.setText(availability.getGround());

        return row;
    }

    public static class ViewHolder {
        private TextView hourTextView;
        private TextView courtTextView;

        public ViewHolder(View view) {
            hourTextView = (TextView) view.findViewById(R.id.availability_list_row_hour);
            courtTextView = (TextView) view.findViewById(R.id.availability_list_row_court);
        }
    }
}
