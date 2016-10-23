package ru.mail.park.dao;

import org.springframework.http.ResponseEntity;
import ru.mail.park.data.UserDataSet;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 21.10.16.
 */

public interface UserDAO {
    ResponseEntity registration(UserDataSet user);

    ResponseEntity getUserInfo(long userId, HttpSession httpSession);

    ResponseEntity changeUserInfo(long userId, UserDataSet changesForUser, HttpSession httpSession);

    ResponseEntity deleteUser(long userId, HttpSession httpSession);
}
