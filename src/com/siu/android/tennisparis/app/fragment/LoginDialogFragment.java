package com.siu.android.tennisparis.app.fragment;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import com.actionbarsherlock.app.SherlockDialogFragment;
import com.siu.android.tennisparis.R;

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

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(STYLE_NO_TITLE, 0);
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
    }

    private void initButtons() {
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
