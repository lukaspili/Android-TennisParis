package com.siu.android.tennisparis.app.fragment;

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
import com.siu.android.tennisparis.model.Reservation;
import com.siu.android.tennisparis.model.User;
import com.siu.android.tennisparis.task.LoginTask;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class ReservationDialogFragment extends SherlockDialogFragment {

    public static final String EXTRA_RESERVATION = "extra_reservation";

    private Button cancelButton;
    private Button submitButton;

    private Reservation reservation;

    private LoginTask loginTask;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setStyle(STYLE_NO_TITLE, 0);
        setRetainInstance(true);

        reservation = (Reservation) getArguments().get(EXTRA_RESERVATION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.reservation_dialog_fragment, container, false);

        cancelButton = (Button) view.findViewById(R.id.reservation_dialog_buttons_cancel);
        submitButton = (Button) view.findViewById(R.id.reservation_dialog_buttons_submit);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initReservation();
        initProgressBar();
        initButtons();
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
                dismiss();
            }
        });
    }

    private void initReservation() {

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
