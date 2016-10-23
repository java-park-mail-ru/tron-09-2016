package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.impl.AuthorizationDAOImpl;
import ru.mail.park.data.SessionDataSet;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.AuthorizationService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    private AuthorizationDAO authorizationDAO;

    @Autowired
    public AuthorizationServiceImpl(AuthorizationDAOImpl authorizationDAOImpl) {
        this.authorizationDAO = authorizationDAOImpl;
    }

    @Override
    public ResponseEntity login(UserDataSet user, HttpSession httpSession) {
        if (StringUtils.isEmpty(user.getLogin())
                || StringUtils.isEmpty(user.getPassword())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }
        final String sessionId = httpSession.getId();
        final SessionDataSet session = authorizationDAO.login(user, sessionId);
        if (session != null) {
            return ResponseEntity.ok(session);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    @Override
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final String sessionId = httpSession.getId();
        final SessionDataSet session = authorizationDAO.authorizationCheck(sessionId);
        if (session != null) {
            return ResponseEntity.ok(session);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @Override
    public ResponseEntity logout(HttpSession httpSession) {
        authorizationDAO.logout(httpSession.getId());

        return ResponseEntity.ok("{}");
    }

}
