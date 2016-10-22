package ru.mail.park.dao;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 21.10.16.
 */

public interface UserDAO {
    ResponseEntity registration(String jsonString);

    ResponseEntity getUserInfo(long userId, HttpSession httpSession);

    ResponseEntity changeUserInfo(long userId, String jsonString, HttpSession httpSession);

    ResponseEntity deleteUser(long userId, HttpSession httpSession);
}
