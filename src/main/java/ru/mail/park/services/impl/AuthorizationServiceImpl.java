package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.responses.SessionReply;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.AuthorizationService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private UserDAO userDAO;

    @Autowired
    public AuthorizationServiceImpl(UserDAOImpl userDAOImpl) {
        this.userDAO = userDAOImpl;
    }

    @Override
    public ResponseEntity login(UserDataSet user, HttpSession httpSession) {
        if (StringUtils.isEmpty(user.getLogin())
                || StringUtils.isEmpty(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        final String sessionId = httpSession.getId();
        final Long userId = userDAO.getIdByLogin(user.getLogin(), user.getPassword());
        if (userId != null) {
            httpSession.setAttribute(sessionId, userId);
            final SessionReply sessionReply = new SessionReply(sessionId, userId);
            return ResponseEntity.ok(sessionReply);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    @Override
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final Object object = httpSession.getAttribute(sessionId);
        if (object instanceof Long) {
            final Long userId = (Long) object;
            final SessionReply sessionReply = new SessionReply(sessionId, userId);
            return ResponseEntity.ok(sessionReply);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @Override
    public ResponseEntity logout(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        httpSession.removeAttribute(sessionId);

        return ResponseEntity.ok("{}");
    }

}
