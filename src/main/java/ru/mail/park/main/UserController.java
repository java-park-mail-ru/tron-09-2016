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
public class UserController {
    private final AccountService accountService;

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(path = "/api/user/id", method = RequestMethod.GET)
    public ResponseEntity getUserInfo(@RequestBody UserRequest body,
                                HttpSession httpSession) {
        System.out.println(body.getId());
        return ResponseEntity.ok("{}");
    }

    private static class UserRequest {
        private String id;

        private UserRequest() {}

        private UserRequest(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }
    }

}
