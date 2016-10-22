package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.impl.AuthorizationDAOImpl;
import ru.mail.park.dataSets.UserDataSet;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

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
    public ResponseEntity login(@RequestBody String body,
                                HttpSession httpSession) {
        return authorizationDAO.login(body, httpSession);
    }

//    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
//    public ResponseEntity authorizationCheck(HttpSession httpSession) {
//        final String sessionId = httpSession.getId();
//        final UserDataSet user = sessionService.getUser(sessionId);
//        if (user != null){
//            return ResponseEntity.ok(Helper.getIdResponse(user.getID()));
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
//    }
//
//    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
//    public ResponseEntity logout(HttpSession httpSession) {
//        final String sessionId = httpSession.getId();
//        sessionService.deleteSession(sessionId);
//        return ResponseEntity.ok("{}");
//    }

}
