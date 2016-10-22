package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.dataSets.UserDataSet;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseController {
    private UserDAO userDAO;

    public UserController(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    void init() {
        super.init();
        userDAO = new UserDAOImpl(dataSource);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@PathVariable long userId, HttpSession httpSession) {
        return userDAO.getUserInfo(userId, httpSession);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity changeUserInfo(@PathVariable long userId,
                                         @RequestBody String body,
                                         HttpSession httpSession) {
        return userDAO.changeUserInfo(userId, body, httpSession);
    }

//    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.DELETE)
//    public ResponseEntity deleteUser(@PathVariable long userId, HttpSession httpSession) {
//        final UserDataSet sessionUser = sessionService.getUser(httpSession.getId());
//        if (sessionUser == null){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
//        }
//        if (sessionUser.getID() != userId){
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
//        }
//
//        accountService.deleteUser(sessionUser.getLogin());
//        sessionService.deleteAllSessions(sessionUser.getLogin());
//
//        return ResponseEntity.ok("{}");
//    }

}
