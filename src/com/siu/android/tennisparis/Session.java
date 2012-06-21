package com.siu.android.tennisparis;

import com.siu.android.tennisparis.model.User;

/**
 * @author Lukasz Piliszczuk <lukasz.pili AT gmail.com>
 */
public final class Session {

    private static Session instance;

    private User user;

    private Session() {
    }

    public static Session getInstance() {
        if (null == instance) {
            instance = new Session();
        }

        return instance;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isLogged() {
        return (null != user);
    }

    public void destroy() {
        user = null;
    }
}
