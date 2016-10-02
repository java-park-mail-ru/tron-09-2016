package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
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
        final UserProfile user = accountService.getUser(body.getLogin());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        if (user.getPassword().equals(body.getPassword())) {
            final String sessionId = httpSession.getId();
            sessionService.addSession(sessionId, user);
            return ResponseEntity.ok("{\n\t\"id\": " + Long.toString(user.getID()) + "\n}");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    private static class LoginRequest {
        private String login;
        private String password;

        private LoginRequest() {}

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
        final UserProfile user = sessionService.getUser(sessionId);
        if (user != null){
            return ResponseEntity.ok("{\n\t\"id\": " + Long.toString(user.getID()) + "\n}");
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
