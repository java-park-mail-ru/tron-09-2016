package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.BadResponse;
import ru.mail.park.responses.Status;
import ru.mail.park.services.UserService;
import ru.mail.park.services.UserServiceImpl;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/user")
@SuppressWarnings("unused")
public class UserController {
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");
    public static final String USER_ID = "userId";

    private final AuthorizationController authorizationController;
    private final UserService userService;

    @Autowired
    public UserController(AuthorizationController authorizationController,
                          UserServiceImpl userServiceImpl) {
        this.authorizationController = authorizationController;
        this.userService = userServiceImpl;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registration(@RequestBody UserDataSet body) {
        final String status = Validation.getLoginPasswordEmailStatus(
                body.getLogin(),
                body.getPassword(),
                body.getEmail()
        );
        if (!status.equals(Validation.CORRECT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(status);
        }

        final UserDataSet userReply = userService.registration(body);
        if (userReply != null) {
            return ResponseEntity.ok(userReply);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@PathVariable long userId, HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationController.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        final UserDataSet user = userService.getUserInfo(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity changeUserInfo(@PathVariable long userId,
                                         @RequestBody UserDataSet body,
                                         HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationController.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = httpSession.getAttribute(USER_ID);
        if (idCheck(object, userId) != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final String status = Validation.getLoginPasswordEmailStatus(
                body.getLogin(),
                body.getPassword(),
                body.getEmail()
        );
        if (!status.equals(Validation.CORRECT)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(status);
        }

        final UserDataSet user = userService.changeUserInfo(userId, body);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable long userId, HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationController.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = httpSession.getAttribute(USER_ID);
        if (idCheck(object, userId) != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final int code = userService.deleteUser(userId);
        if (code == Status.OK) {
            httpSession.removeAttribute(USER_ID);
            return ResponseEntity.ok("{}");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    private static int idCheck(Object object, long userId) {
        if (object instanceof Long) {
            final Long sessionUserId = (Long) object;
            if (sessionUserId != userId) {
                return Status.ERROR;
            }
        } else {
            return Status.ERROR;
        }

        return Status.OK;
    }

}
