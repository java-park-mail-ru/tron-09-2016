package ru.mail.park.dao;

import ru.mail.park.data.UserDataSet;

/**
 * Created by zac on 21.10.16.
 */

public interface UserDAO {
    UserDataSet registration(UserDataSet user);

    UserDataSet getUserInfo(long userId, String sessionId);

    UserDataSet changeUserInfo(long userId, UserDataSet changesForUser, String sessionId);

    int deleteUser(long userId, String sessionId);

    Long getIdByLogin(String login, String password);
}
