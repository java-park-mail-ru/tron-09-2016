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
public class UserController {
    private final AccountService accountService;
    private final SessionService sessionService;
    private final String badResponse =
            "{\n" +
            "  \"status\": 403,\n" +
            "  \"message\": \"Чужой юзер\"\n" +
            "}";

    @Autowired
    public UserController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@PathVariable long userId,
                                      HttpSession httpSession) {
        final UserProfile sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
        if (sessionUser.getID() != userId){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        return ResponseEntity.ok(
                "{\n" +
                "  \"id\": " + Long.toString(userId) + ",\n" +
                "  \"login\": \"" + sessionUser.getLogin() + "\",\n" +
                "  \"email\": \"" + sessionUser.getEmail() + "\"\n" +
                "}");
    }

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity putUserInfo(@PathVariable long userId,
                                      @RequestBody UserRequest body,
                                      HttpSession httpSession) {
        final UserProfile sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badResponse);
        }
        if (sessionUser.getID() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badResponse);
        }

        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();

        final boolean isLoginSame = login.equals(sessionUser.getLogin());
        final boolean isEmailSame = email.equals(sessionUser.getEmail());
        if (isLoginSame && isEmailSame) {
            sessionUser.setPassword(password);
            return ResponseEntity.ok(
                    "{\n" +
                    "  \"id\": \"" + Long.toString(userId) + "\"\n" +
                    "}");
        }

        final boolean isEmailNotSameAndFree = !isEmailSame && accountService.isEmailFree(email);
        if (isLoginSame && isEmailNotSameAndFree) {
            sessionUser.setPassword(password);
            sessionUser.setEmail(email);
            return ResponseEntity.ok(
                    "{\n" +
                    "  \"id\": \"" + Long.toString(userId) + "\"\n" +
                    "}");
        }

        final boolean isLoginNotSameAndFree = !isLoginSame && accountService.isLoginFree(login);
        if (isEmailSame && isLoginNotSameAndFree) {
            accountService.deletePairByKey(login);
            sessionUser.setLogin(login);
            sessionUser.setPassword(password);
            accountService.addUser(sessionUser);
            return ResponseEntity.ok(
                    "{\n" +
                    "  \"id\": \"" + Long.toString(userId) + "\"\n" +
                    "}");
        }
        if (isLoginNotSameAndFree && isEmailNotSameAndFree) {
            accountService.deletePairByKey(login);
            sessionUser.setLogin(login);
            sessionUser.setPassword(password);
            sessionUser.setEmail(email);
            accountService.addUser(sessionUser);
            return ResponseEntity.ok(
                    "{\n" +
                    "  \"id\": \"" + Long.toString(userId) + "\"\n" +
                    "}");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badResponse);
    }

    private static class UserRequest {
        private String login;
        private String password;
        private String email;

        private UserRequest() {}

        private UserRequest(String login, String password, String email) {
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

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable long userId,
                                      HttpSession httpSession) {
        final UserProfile sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badResponse);
        }
        if (sessionUser.getID() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(badResponse);
        }

        accountService.deletePairByKey(sessionUser.getLogin());
        sessionService.deleteSession(httpSession.getId());

        return ResponseEntity.ok("{}");
    }

}
