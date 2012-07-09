package com.siu.android.tennisparis.app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import com.siu.android.tennisparis.R;
import com.siu.android.tennisparis.Session;
import com.siu.android.tennisparis.app.activity.AccountActivity;
import com.siu.android.tennisparis.model.User;
import com.siu.android.tennisparis.task.LoginTask;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class LoginDialogWrapper {

    private Dialog dialog;

    private EditText loginEditText;
    private EditText passwordEditText;
    private CheckBox rememberCheckBox;
    private Button cancelButton;
    private Button submitButton;

    private LoginTask loginTask;
    private ProgressDialog progressDialog;

    public LoginDialogWrapper(Dialog dialog) {
        this.dialog = dialog;

        loginEditText = (EditText) dialog.findViewById(R.id.login_dialog_fragment_login);
        passwordEditText = (EditText) dialog.findViewById(R.id.login_dialog_fragment_password);
        rememberCheckBox = (CheckBox) dialog.findViewById(R.id.login_dialog_fragment_remember_checkbox);
        cancelButton = (Button) dialog.findViewById(R.id.login_dialog_fragment_button_cancel);
        submitButton = (Button) dialog.findViewById(R.id.login_dialog_fragment_button_submit);

        initButtons();
        initProgressBar();
    }

    private void initButtons() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login = loginEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (StringUtils.isBlank(login) || StringUtils.isBlank(password)) {
                    if (StringUtils.isBlank(login)) {
                        loginEditText.setHintTextColor(dialog.getContext().getResources().getColor(R.color.red_light));
                    }

                    if (StringUtils.isBlank(login)) {
                        passwordEditText.setHintTextColor(dialog.getContext().getResources().getColor(R.color.red_light));
                    }

                    return;
                }

                progressDialog.show();
                loginTask = new LoginTask(login, password, LoginDialogWrapper.this);
                loginTask.execute();
            }
        });
    }

    private void initProgressBar() {
        progressDialog = new ProgressDialog(dialog.getContext());
        progressDialog.setTitle(R.string.login_dialog_progress_title);
        progressDialog.setMessage(dialog.getContext().getResources().getString(R.string.login_dialog_progress_message));
    }

    /* Login task */

    public void onLoginTaskFinished(User user) {
        progressDialog.dismiss();
        loginTask = null;

        if (null == user) {
            Toast.makeText(dialog.getContext(), "Identifiants invalides ou impossible de se connecter", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(dialog.getContext(), "Authentification réussie avec succès !", Toast.LENGTH_LONG).show();
        Session.getInstance().setUser(user);

        dialog.getContext().startActivity(new Intent(dialog.getContext(), AccountActivity.class));
        dialog.dismiss();
    }
}
