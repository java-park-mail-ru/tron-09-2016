package ru.mail.park.services;

import org.springframework.http.ResponseEntity;
import ru.mail.park.data.UserDataSet;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */
public interface AuthorizationService {
    ResponseEntity login(UserDataSet user, HttpSession httpSession);

    ResponseEntity authorizationCheck(HttpSession httpSession);

    ResponseEntity logout(HttpSession httpSession);
}
