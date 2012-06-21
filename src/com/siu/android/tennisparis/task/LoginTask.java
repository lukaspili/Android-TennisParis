package com.siu.android.tennisparis.task;

import android.os.AsyncTask;
import com.siu.android.tennisparis.app.fragment.LoginDialogFragment;
import com.siu.android.tennisparis.model.User;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public class LoginTask extends AsyncTask<Void, Void, User> {

    private String login;
    private String password;
    private LoginDialogFragment fragment;

    public LoginTask(String login, String password, LoginDialogFragment fragment) {
        this.login = login;
        this.password = password;
        this.fragment = fragment;
    }

    @Override
    protected User doInBackground(Void... voids) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        User user = new User(login, password);
        user.setName("Lukasz Piliszczuk");
        user.setAddress("77 rue notre dame des champs, 75006 Paris");
        user.setPhone("0123456789");

        return user;
    }

    @Override
    protected void onPostExecute(User user) {
        fragment.onLoginTaskFinish(user);
    }
}
