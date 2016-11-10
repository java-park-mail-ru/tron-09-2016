package ru.mail.park.controllers;

import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * Created by zac on 10.11.16.
 */

public class Validation {
    public static final String CORRECT = "Correct";
    public static final int LOGIN_MIN_LENGTH = 3;
    public static final int LOGIN_MAX_LENGTH = 30;
    public static final int PASSWORD_MIN_LENGTH = 4;
    public static final int PASSWORD_MAX_LENGTH = 30;
    public static final int EMAIL_MAX_LENGTH = 50;
    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^([a-z0-9_\\\\.-]+)@([a-z0-9_\\\\.-]+)\\.([a-z\\\\.]+)$", Pattern.CASE_INSENSITIVE);

    public static String getLoginStatus(String login) {
        if (StringUtils.isEmpty(login)) {
            return "Login is empty";
        }

        if (login.length() < LOGIN_MIN_LENGTH) {
            return "Login can't be less than " + LOGIN_MIN_LENGTH + "symbols";
        }

        if (login.length() > LOGIN_MAX_LENGTH) {
            return "Login can't be more than " + LOGIN_MAX_LENGTH + "symbols";
        }

        return CORRECT;
    }

    public static String getPasswordStatus(String password) {
        if (StringUtils.isEmpty(password)) {
            return "Password is empty";
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            return "Password can't be less than " + PASSWORD_MIN_LENGTH + "symbols";
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            return "Password can't be more than " + PASSWORD_MAX_LENGTH + "symbols";
        }

        return CORRECT;
    }

    public static String getEmailStatus(String email) {
        if (StringUtils.isEmpty(email)) {
            return "Email is empty";
        }

        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return "Email isn't correct";
        }

        if (email.length() > EMAIL_MAX_LENGTH) {
            return "Email can't be more than " + EMAIL_MAX_LENGTH + "symbols";
        }

        return CORRECT;
    }

    public static String getLoginPasswordStatus(String login, String password) {
        final String status = getLoginStatus(login);
        if (!status.equals(CORRECT)) {
            return status;
        }

        return getPasswordStatus(password);
    }

    public static String getLoginPasswordEmailStatus(String login, String password, String email) {
        final String status = getLoginPasswordStatus(login, password);
        if (!status.equals(CORRECT)) {
            return status;
        }

        return getEmailStatus(email);
    }

}
