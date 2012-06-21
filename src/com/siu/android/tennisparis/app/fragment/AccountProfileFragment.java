package com.siu.android.tennisparis.app.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.model.User;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AccountProfileFragment extends Fragment {

    private TextView nameTextView;
    private TextView addressTextView;
    private TextView phoneTextView;

    private User user;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        user = Session.getInstance().getUser();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.account_profile_fragment, viewGroup, false);

        nameTextView = (TextView) view.findViewById(R.id.account_profile_name);
        addressTextView = (TextView) view.findViewById(R.id.account_profile_address);
        phoneTextView = (TextView) view.findViewById(R.id.account_profile_phone);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        initUser();
    }

    private void initUser() {
        nameTextView.setText(user.getName());
        addressTextView.setText(user.getAddress());
        phoneTextView.setText(user.getPhone());
    }
}
