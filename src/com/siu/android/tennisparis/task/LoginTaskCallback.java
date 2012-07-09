package com.siu.android.tennisparis.task;

import com.siu.android.tennisparis.model.User;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public interface LoginTaskCallback {

    public void onLoginTaskFinished(User user);
}
