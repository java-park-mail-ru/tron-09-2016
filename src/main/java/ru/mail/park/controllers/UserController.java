package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dataSets.UserDataSet;
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
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");

    @Autowired
    public UserController(AccountService accountService, SessionService sessionService) {
        this.accountService = accountService;
        this.sessionService = sessionService;
    }

    private static final class BadResponse {
        private int status;
        private String message;

        @SuppressWarnings("unused")
        private BadResponse() {
        }

        private BadResponse(int status, String message) {
            this.status = status;
            this.message = message;
        }

        @SuppressWarnings("unused")
        public int getStatus() {
            return status;
        }

        @SuppressWarnings("unused")
        public String getMessage() {
            return message;
        }
    }

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@PathVariable long userId, HttpSession httpSession) {
        final UserDataSet sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }
        final UserDataSet user = accountService.getUserBYId(userId);
        if (user != null) {
            return ResponseEntity.ok(
                    new SuccessResponse(Long.toString(userId), user.getLogin(), user.getEmail()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    private static final class SuccessResponse {
        private String id;
        private String login;
        private String email;

        @SuppressWarnings("unused")
        private SuccessResponse() {
        }

        private SuccessResponse(String id, String login, String email) {
            this.id = id;
            this.login = login;
            this.email = email;
        }

        @SuppressWarnings("unused")
        public String getId() {
            return id;
        }

        @SuppressWarnings("unused")
        public String getLogin() {
            return login;
        }

        @SuppressWarnings("unused")
        public String getEmail() {
            return email;
        }
    }

    @RequestMapping(value = "/api/user/{userId}", method = RequestMethod.PUT)
    public ResponseEntity changeUserInfo(@PathVariable long userId,
                                         @RequestBody UserRequest body,
                                         HttpSession httpSession) {
        final UserDataSet sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        if (sessionUser.getID() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        final String login = body.getLogin();
        final String password = body.getPassword();
        final String email = body.getEmail();

        final boolean isLoginSame = login.equals(sessionUser.getLogin());
        final boolean isEmailSame = email.equals(sessionUser.getEmail());

        if (isLoginSame && isEmailSame) {
            sessionUser.setPassword(password);
            return ResponseEntity.ok(Helper.getIdResponse(userId));
        }

        final boolean isEmailNotSameAndFree = !isEmailSame && accountService.isEmailFree(email);
        if (isLoginSame && isEmailNotSameAndFree) {
            sessionUser.setPassword(password);
            sessionUser.setEmail(email);
            return ResponseEntity.ok(Helper.getIdResponse(userId));
        }

        final UserDataSet existingUser = accountService.getUser(login);
        final boolean isLoginNotSameAndFree = (existingUser == null && !isLoginSame);

        if (isLoginNotSameAndFree && (isEmailSame || isEmailNotSameAndFree)) {
            accountService.deleteUser(login);
            sessionUser.setLogin(login);
            sessionUser.setPassword(password);
            sessionUser.setEmail(email);
            accountService.addUser(sessionUser);
            return ResponseEntity.ok(Helper.getIdResponse(userId));
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    private static final class UserRequest {
        private String login;
        private String password;
        private String email;

        @SuppressWarnings("unused")
        private UserRequest() {}

        @SuppressWarnings("unused")
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
    public ResponseEntity deleteUser(@PathVariable long userId, HttpSession httpSession) {
        final UserDataSet sessionUser = sessionService.getUser(httpSession.getId());
        if (sessionUser == null){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        if (sessionUser.getID() != userId){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        accountService.deleteUser(sessionUser.getLogin());
        sessionService.deleteAllSessions(sessionUser.getLogin());

        return ResponseEntity.ok("{}");
    }

}
