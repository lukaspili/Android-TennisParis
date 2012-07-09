package com.siu.android.tennisparis.app.dialog;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.app.activity.AccountActivity;
import com.siu.android.tennisparis.model.User;
import com.siu.android.tennisparis.task.LoginTask;
import com.siu.android.tennisparis.task.LoginTaskCallback;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class LoginDialogFragment extends SherlockDialogFragment {

    public static final String EXTRA_AVAILABILITY = "extra_availability";

    private EditText loginEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private Button cancelButton;
    private Button submitButton;

    private LoginTask loginTask;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_dialog_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getDialog().setTitle(R.string.login_dialog_title);

        new LoginDialogWrapper(getDialog());
    }
}
