package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.UserDAOImpl;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.IdReply;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/session")
public class AuthorizationController {
    public static final String USER_ID = "userId";
    private UserDAO userDAO;

    @Autowired
    public AuthorizationController(UserDAOImpl userDAOImpl) {
        this.userDAO = userDAOImpl;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody UserDataSet body, HttpSession httpSession) {
        final String status = Validation.getLoginPasswordStatus(body.getLogin(), body.getPassword());
        if (!status.equals(Validation.CORRECT)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }

        final Long userId = userDAO.getIdByLogin(body.getLogin(), body.getPassword());
        if (userId != null) {
            httpSession.setAttribute(USER_ID, userId);
            final IdReply idReply = new IdReply(userId);
            return ResponseEntity.ok(idReply);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final Object object = httpSession.getAttribute(USER_ID);
        if (object instanceof Long) {
            final Long userId = (Long) object;
            final IdReply idReply = new IdReply(userId);
            return ResponseEntity.ok(idReply);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public String logout(HttpSession httpSession) {
        httpSession.removeAttribute(USER_ID);

        return "{}";
    }

}
