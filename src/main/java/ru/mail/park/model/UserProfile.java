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
    private long id;

    public UserProfile(String login, String password, String email) {
        this.login = login;
        this.password = password;
        this.email = email;
        this.id = ID_GENETATOR.getAndIncrement();
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public long getID() {
        return id;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
