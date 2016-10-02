package ru.mail.park.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Zac on 01/10/16.
 */
public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);

    private String login;
    private String password;
    private String email;
    private long ID;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.ID = ID_GENETATOR.getAndIncrement();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public long getID() {
        return ID;
    }
}
