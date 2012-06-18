package com.siu.android.tennisparis.util;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import com.siu.android.tennisparis.app.fragment.LoginDialogFragment;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public final class FragmentUtils {

    public static void showDialog(FragmentManager fragmentManager, DialogFragment dialogFragment) {
        FragmentTransaction ft = fragmentManager.beginTransaction();

        Fragment prev = fragmentManager.findFragmentByTag("Dialog");
        if (prev != null) {
            ft.remove(prev);
        }

        ft.addToBackStack(null);
        dialogFragment.show(ft, "Dialog");
    }
}
