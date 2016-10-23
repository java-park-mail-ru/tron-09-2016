package ru.mail.park.dao;

import org.springframework.http.ResponseEntity;
import ru.mail.park.data.UserDataSet;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 21.10.16.
 */

public interface AuthorizationDAO {
    ResponseEntity login(UserDataSet user, HttpSession httpSession);

    ResponseEntity authorizationCheck(HttpSession httpSession);

    ResponseEntity logout(HttpSession httpSession);
}
