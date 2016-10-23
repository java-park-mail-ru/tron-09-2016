package ru.mail.park.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.impl.AuthorizationDAOImpl;
import ru.mail.park.data.UserDataSet;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/session")
public class AuthorizationController extends BaseController {
    private AuthorizationDAO authorizationDAO;

    public AuthorizationController(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    void init() {
        super.init();
        authorizationDAO = new AuthorizationDAOImpl(dataSource);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserDataSet body, HttpSession httpSession) {
        return authorizationDAO.login(body, httpSession);
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        return authorizationDAO.authorizationCheck(httpSession);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {
        return authorizationDAO.logout(httpSession);
    }

}
