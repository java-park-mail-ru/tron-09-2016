package ru.mail.park.model;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Solovyev on 17/09/16.
 */
public class UserProfile {
    private static final AtomicLong ID_GENETATOR = new AtomicLong(0);

    private String login;
    private String email;
    private String password;
    private long ID;

    public UserProfile(String login, String email, String password) {
        this.login = login;
        this.email = email;
        this.password = password;
        this.ID = ID_GENETATOR.getAndIncrement();
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public long getID() {
        return ID;
    }
}
