package com.siu.android.tennisparis.app.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.siu.android.tennisparis.task.LoginTaskCallback;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class LoginDialog extends Dialog {

    public LoginDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_dialog_fragment);
        setTitle(R.string.login_dialog_title);

        new LoginDialogWrapper(this);
    }
}
