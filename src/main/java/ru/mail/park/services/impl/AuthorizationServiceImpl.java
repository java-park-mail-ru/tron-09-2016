package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.responses.IdReply;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.AuthorizationService;

import javax.servlet.http.HttpSession;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class AuthorizationServiceImpl implements AuthorizationService {
    public static final String USER_ID = "userId";
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

        final Long userId = userDAO.getIdByLogin(user.getLogin(), user.getPassword());
        if (userId != null) {
            httpSession.setAttribute(USER_ID, userId);
            final IdReply idReply = new IdReply(userId);
            return ResponseEntity.ok(idReply);
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
    }

    @Override
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final Object object = httpSession.getAttribute(USER_ID);
        if (object instanceof Long) {
            final Long userId = (Long) object;
            final IdReply idReply = new IdReply(userId);
            return ResponseEntity.ok(idReply);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
    }

    @Override
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.removeAttribute(USER_ID);

        return ResponseEntity.ok("{}");
    }

}
