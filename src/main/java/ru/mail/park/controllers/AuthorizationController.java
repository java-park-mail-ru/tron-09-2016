package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.AuthorizationService;
import ru.mail.park.services.impl.AuthorizationServiceImpl;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/session")
@SuppressWarnings("unused")
public class AuthorizationController {
    private final AuthorizationService authorizationService;

    @Autowired
    public AuthorizationController(AuthorizationServiceImpl authorizationServiceImpl) {
        this.authorizationService = authorizationServiceImpl;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserDataSet body, HttpSession httpSession) {
        return authorizationService.login(body, httpSession);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        return authorizationService.authorizationCheck(httpSession);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {
        return authorizationService.logout(httpSession);
    }

}
