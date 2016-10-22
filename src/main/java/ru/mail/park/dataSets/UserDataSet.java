package ru.mail.park.dataSets;

import com.google.gson.JsonObject;

/**
 * Created by Zac on 01/10/16.
 */

public class UserDataSet {
    private long id;
    private String login;
    private String password;
    private String email;

    public UserDataSet(JsonObject object) {
        id = -1;
        login = object.get("login").getAsString();
        password = object.get("password").getAsString();
        email = object.get("email").getAsString();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
