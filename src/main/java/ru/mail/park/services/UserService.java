package ru.mail.park.services;

import ru.mail.park.data.UserDataSet;

/**
 * Created by zac on 23.10.16.
 */
public interface UserService {
    UserDataSet registration(UserDataSet user);

    UserDataSet getUserInfo(long userId);

    UserDataSet changeUserInfo(long userId, UserDataSet changesForUser);

    int deleteUser(long userId);
}
