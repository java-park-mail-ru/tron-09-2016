package ru.mail.park.dao;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 21.10.16.
 */

public interface UserDAO {
    ResponseEntity getUserInfo(long userId, HttpSession httpSession);
}
