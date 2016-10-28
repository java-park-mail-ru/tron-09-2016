package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.BadResponse;
import ru.mail.park.responses.Status;
import ru.mail.park.services.AuthorizationService;
import ru.mail.park.services.UserService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class UserServiceImpl implements UserService {
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");
    public static final String USER_ID = "userId";

    private UserDAO userDAO;
    private AuthorizationService authorizationService;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAOImpl, AuthorizationServiceImpl authorizationService) {
        this.userDAO = userDAOImpl;
        this.authorizationService = authorizationService;
    }

    @Override
    public ResponseEntity registration(UserDataSet user) {
        if (StringUtils.isEmpty(user.getLogin())
                || StringUtils.isEmpty(user.getPassword())
                || StringUtils.isEmpty(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(user);
        }

        final UserDataSet userReply = userDAO.registration(user);
        if (userReply != null) {
            return ResponseEntity.ok(userReply);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{Возник SQLException}");
    }

    @Override
    public ResponseEntity getUserInfo(long userId, HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationService.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        final UserDataSet user = userDAO.getUserInfo(userId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @Override
    public ResponseEntity changeUserInfo(long userId,
                                         UserDataSet changesForUser,
                                         HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationService.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = httpSession.getAttribute(USER_ID);
        if (idCheck(object, userId) != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        if (StringUtils.isEmpty(changesForUser.getLogin())
                || StringUtils.isEmpty(changesForUser.getPassword())
                || StringUtils.isEmpty(changesForUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final UserDataSet user = userDAO.changeUserInfo(userId, changesForUser);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    @Override
    public ResponseEntity deleteUser(long userId, HttpSession httpSession) {
        final ResponseEntity responseEntity = authorizationService.authorizationCheck(httpSession);
        if (responseEntity.getStatusCodeValue() != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = httpSession.getAttribute(USER_ID);
        if (idCheck(object, userId) != Status.OK) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final int code = userDAO.deleteUser(userId);
        if (code == Status.OK) {
            httpSession.removeAttribute(USER_ID);
            return ResponseEntity.ok("{}");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    public int idCheck(Object object, long userId) {
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
