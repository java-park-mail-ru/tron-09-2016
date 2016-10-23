package ru.mail.park.services;

import org.springframework.http.ResponseEntity;
import ru.mail.park.data.UserDataSet;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */
public interface UserService {
    ResponseEntity registration(UserDataSet user);

    ResponseEntity getUserInfo(long userId, HttpSession httpSession);

    ResponseEntity changeUserInfo(long userId, UserDataSet changesForUser, HttpSession httpSession);

    ResponseEntity deleteUser(long userId, HttpSession httpSession);
}
