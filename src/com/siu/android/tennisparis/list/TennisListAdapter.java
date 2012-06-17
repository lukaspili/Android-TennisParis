package com.siu.android.tennisparis.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.dao.model.Tennis;

import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class TennisListAdapter extends ArrayAdapter<Tennis> {

    public TennisListAdapter(Context context, List<Tennis> tennises) {
        super(context, android.R.layout.simple_list_item_1, tennises);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(R.layout.tennis_list_row, parent, false);
        }

        ViewHolder viewHolder = (ViewHolder) row.getTag();

        if (null == viewHolder) {
            viewHolder = new ViewHolder(row);
            row.setTag(viewHolder);
        }

        Tennis tennis = getItem(position);

        viewHolder.name.setText(tennis.getName());
        viewHolder.address.setText(tennis.getAddress() + ", " + tennis.getPostalCode());

        return row;
    }

    private class ViewHolder {

        private TextView name;
        private TextView address;

        private ViewHolder(View view) {
            name = (TextView) view.findViewById(R.id.tennis_list_row_text1);
            address = (TextView) view.findViewById(R.id.tennis_list_row_text2);
        }
    }
}
