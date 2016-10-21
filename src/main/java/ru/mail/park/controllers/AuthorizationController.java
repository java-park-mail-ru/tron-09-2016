package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dataSets.UserDataSet;
import ru.mail.park.services.AccountService;
import ru.mail.park.services.SessionService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
public class AuthorizationController {
    private final AccountService accountService;
    private final SessionService sessionService;

    @Autowired
    public AuthorizationController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody LoginRequest body,
                                HttpSession httpSession) {
        if (StringUtils.isEmpty(body.getLogin())
                || StringUtils.isEmpty(body.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        final UserDataSet user = accountService.getUser(body.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (user.getPassword().equals(body.getPassword())) {
            final String sessionId = httpSession.getId();
            sessionService.addSession(sessionId, user);
            return ResponseEntity.ok(Helper.getIdResponse(user.getID()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    private static final class LoginRequest {
        private String login;
        private String password;

        @SuppressWarnings("unused")
        private LoginRequest() {}

        @SuppressWarnings("unused")
        private LoginRequest(String login, String password) {
            this.login = login;
            this.password = password;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.GET)
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final UserDataSet user = sessionService.getUser(sessionId);
        if (user != null){
            return ResponseEntity.ok(Helper.getIdResponse(user.getID()));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.DELETE)
    public ResponseEntity logout(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        sessionService.deleteSession(sessionId);
        return ResponseEntity.ok("{}");
    }

}
