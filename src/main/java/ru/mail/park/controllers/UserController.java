package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.UserService;
import ru.mail.park.services.impl.UserServiceImpl;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 02.10.16.
 */

@RestController
@RequestMapping(value = "/api/user")
@SuppressWarnings("unused")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userService = userServiceImpl;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registration(@RequestBody UserDataSet body) {
        return userService.registration(body);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@PathVariable long userId, HttpSession httpSession) {
        return userService.getUserInfo(userId, httpSession);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
    public ResponseEntity changeUserInfo(@PathVariable long userId,
                                         @RequestBody UserDataSet body,
                                         HttpSession httpSession) {
        return userService.changeUserInfo(userId, body, httpSession);
    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity deleteUser(@PathVariable long userId, HttpSession httpSession) {
        return userService.deleteUser(userId, httpSession);
    }

}
