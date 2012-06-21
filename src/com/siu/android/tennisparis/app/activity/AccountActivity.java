package com.siu.android.tennisparis.app.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.siu.android.tennisparis.Application;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.app.fragment.AccountProfileFragment;
import com.siu.android.tennisparis.app.fragment.AccountReservationsFragment;
import com.siu.android.tennisparis.dao.model.Tennis;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class AccountActivity extends SherlockFragmentActivity {

    private AlertDialog logoutAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.account_activity);

        initActionBar();

        if (null != savedInstanceState) {
            getSupportActionBar().setSelectedNavigationItem(savedInstanceState.getInt("tab"));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.account_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            case R.id.menu_logout:
                showLogoutDialog();
                break;
        }

        return true;
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        actionBar.setListNavigationCallbacks(new ArrayAdapter<Tab>(this, android.R.layout.simple_spinner_dropdown_item, Tab.values()), new ActionBar.OnNavigationListener() {
            @Override
            public boolean onNavigationItemSelected(int itemPosition, long itemId) {
                Tab tab = Tab.values()[itemPosition];
                Fragment fragment = null;

                switch (tab) {
                    case PROFILE:
                        fragment = new AccountProfileFragment();
                        break;
                    case RESERVATIONS:
                        fragment = new AccountReservationsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.account_activity_frame, fragment).commit();
                return true;
            }
        });
    }

    private void showLogoutDialog() {
        if (null == logoutAlertDialog) {
            logoutAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.logout_dialog_title)
                    .setMessage(R.string.logout_dialog_message)
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    })
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Session.getInstance().destroy();
                            dialogInterface.dismiss();
                            finish();
                        }
                    })
                    .create();
        }

        logoutAlertDialog.show();
    }

    public static enum Tab {
        PROFILE(Application.getContext().getString(R.string.account_tab_profile)), RESERVATIONS(Application.getContext().getString(R.string.account_tab_reservations));

        private String title;

        private Tab(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
