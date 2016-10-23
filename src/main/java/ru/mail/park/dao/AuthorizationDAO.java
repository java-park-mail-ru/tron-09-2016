package ru.mail.park.dao;

import ru.mail.park.data.SessionDataSet;
import ru.mail.park.data.UserDataSet;

/**
 * Created by zac on 21.10.16.
 */

public interface AuthorizationDAO {
    SessionDataSet login(UserDataSet user, String sessionId);

    SessionDataSet authorizationCheck(String sessionId);

    void logout(String sessionId);
}
