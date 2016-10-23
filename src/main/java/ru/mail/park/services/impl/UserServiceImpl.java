package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.AuthorizationDAOImpl;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.data.SessionDataSet;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.BadResponse;
import ru.mail.park.responses.Status;
import ru.mail.park.services.UserService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class UserServiceImpl implements UserService {
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");

    private UserDAO userDAO;
    private AuthorizationDAO authorizationDAO;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAOImpl, AuthorizationDAOImpl authorizationDAOImpl) {
        this.userDAO = userDAOImpl;
        this.authorizationDAO = authorizationDAOImpl;
    }

    @Override
    public ResponseEntity registration(UserDataSet user) {
        if (StringUtils.isEmpty(user.getLogin())
                || StringUtils.isEmpty(user.getPassword())
                || StringUtils.isEmpty(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        final UserDataSet userReply = userDAO.registration(user);
        if (userReply != null) {
            return ResponseEntity.ok(userReply);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
    }

    @Override
    public ResponseEntity getUserInfo(long userId, HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final SessionDataSet session = authorizationDAO.authorizationCheck(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        final UserDataSet user = userDAO.getUserInfo(userId, sessionId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @Override
    public ResponseEntity changeUserInfo(long userId,
                                         UserDataSet changesForUser,
                                         HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final SessionDataSet session = authorizationDAO.authorizationCheck(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        if (session.getUserId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        if (StringUtils.isEmpty(changesForUser.getLogin())
                || StringUtils.isEmpty(changesForUser.getPassword())
                || StringUtils.isEmpty(changesForUser.getEmail())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final UserDataSet user = userDAO.changeUserInfo(userId, changesForUser, sessionId);
        if (user != null) {
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }

    @Override
    public ResponseEntity deleteUser(long userId, HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final SessionDataSet session = authorizationDAO.authorizationCheck(sessionId);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }
        if (session.getUserId() != userId) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final int code = userDAO.deleteUser(userId, sessionId);
        if (code == Status.OK) {
            return ResponseEntity.ok("{}");
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
    }
}
