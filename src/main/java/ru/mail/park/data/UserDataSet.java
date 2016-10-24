package ru.mail.park.data;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Zac on 01/10/16.
 */

public class UserDataSet {
    private Long id;
    private String login;
    private String password;
    private String email;

    @SuppressWarnings("unused")
    public UserDataSet() {

    }

    public UserDataSet(Long id, String login, String password, String email) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.email = email;
    }

    public UserDataSet(ResultSet resultSet) throws SQLException {
        id = resultSet.getLong("id");
        login = resultSet.getString("login");
        password = resultSet.getString("password");
        email = resultSet.getString("email");
    }

    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    @SuppressWarnings("unused")
    public void setLogin(String login) {
        this.login = login;
    }

    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }
}
