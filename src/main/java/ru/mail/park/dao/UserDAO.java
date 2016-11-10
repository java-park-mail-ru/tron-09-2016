package ru.mail.park.dao;

import ru.mail.park.data.UserDataSet;

/**
 * Created by zac on 21.10.16.
 */

public interface UserDAO {
    UserDataSet registration(UserDataSet user);

    UserDataSet getUserInfo(long userId);

    UserDataSet changeUserInfo(long userId, UserDataSet changesForUser);

    int deleteUser(long userId);

    Long getIdByLogin(String login, String password);
}
