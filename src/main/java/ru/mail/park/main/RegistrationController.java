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
 * Created by Solovyev on 06/09/16.
 */

@RestController
public class RegistrationController {
    private final AccountService accountService;

    @Autowired
    public RegistrationController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/user", method = RequestMethod.POST)
    public ResponseEntity login(@RequestBody RegistrationRequest body,
                                HttpSession httpSession) {
        final String sessionId = httpSession.getId();

        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }
        final UserProfile existingUser = accountService.getUser(login);
        if (existingUser != null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        final UserProfile newUser = new UserProfile(login, password, email);
        accountService.addUser(newUser);
        return ResponseEntity.ok(new SuccessResponse(Long.toString(newUser.getID())));
    }

    @RequestMapping(path = "/api/session", method = RequestMethod.POST)
    public ResponseEntity auth(@RequestBody AuthorizationRequest body,
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
            return ResponseEntity.ok(new SuccessResponse(Long.toString(user.getID())));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    private static class RegistrationRequest {
        private String login;
        private String password;
        private String email;

        private RegistrationRequest() {}

        private RegistrationRequest(String login, String password, String email) {
            this.login = login;
            this.password = password;
            this.email = email;
        }

        public String getLogin() {
            return login;
        }

        public String getPassword() {
            return password;
        }

        public String getEmail() {
            return email;
        }
    }

    private static class AuthorizationRequest {
        private String login;
        private String password;

        private AuthorizationRequest() {}

        private AuthorizationRequest(String login, String password) {
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

    private static final class SuccessResponse {
        private String ID;

        private SuccessResponse(String ID) {
            this.ID = ID;
        }

        @SuppressWarnings("unused")
        public String getLogin() {
            return ID;
        }
    }

}
