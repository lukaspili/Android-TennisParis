package com.siu.android.tennisparis.app.fragment;

import android.app.Dialog;
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
        setStyle(STYLE_NO_TITLE, 0);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_dialog_fragment, container, false);

        loginEditText = (EditText) view.findViewById(R.id.login_dialog_fragment_login);
        passwordEditText = (EditText) view.findViewById(R.id.login_dialog_fragment_password);
        rememberCheckBox = (CheckBox) view.findViewById(R.id.login_dialog_fragment_remember_checkbox);
        cancelButton = (Button) view.findViewById(R.id.login_dialog_fragment_button_cancel);
        submitButton = (Button) view.findViewById(R.id.login_dialog_fragment_button_submit);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initButtons();
        initProgressBar();
    }

    private void initButtons() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
                    if (StringUtils.isBlank(login)) {
                        loginEditText.setHintTextColor(getResources().getColor(R.color.red_light));
                    }

                    if (StringUtils.isBlank(login)) {
                        passwordEditText.setHintTextColor(getResources().getColor(R.color.red_light));
                    }

                    return;
                }

                progressDialog.show();
                loginTask = new LoginTask(login, password, LoginDialogFragment.this);
                loginTask.execute();
            }
        });
    }

    private void initProgressBar() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle(R.string.login_dialog_progress_title);
        progressDialog.setMessage(getResources().getString(R.string.login_dialog_progress_message));
    }


    /* Login task */

    public void onLoginTaskFinish(User user) {
        progressDialog.dismiss();
        loginTask = null;

        if (null == user) {
            Toast.makeText(getActivity(), "Identifiants invalides ou impossible de se connecter", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(getActivity(), "Authentification réussie avec succès !", Toast.LENGTH_LONG).show();
        Session.getInstance().setUser(user);

        startActivity(new Intent(getActivity(), AccountActivity.class));
        dismiss();
    }
}
