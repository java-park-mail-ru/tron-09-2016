package ru.mail.park.main;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.model.UserProfile;
import ru.mail.park.services.AccountService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
public class AuthorizationController {
    private final AccountService accountService;

    @Autowired
    public AuthorizationController(AccountService accountService) {
        this.accountService = accountService;
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
}
