package ru.mail.park.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dao.impl.UserDAOImpl;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.services.UserService;

/**
 * Created by zac on 23.10.16.
 */

@Service
public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAOImpl userDAOImpl) {
        this.userDAO = userDAOImpl;
    }

    @Override
    public UserDataSet registration(UserDataSet user) {
        return userDAO.registration(user);
    }

    @Override
    public UserDataSet getUserInfo(long userId) {
        return userDAO.getUserInfo(userId);
    }

    @Override
    public UserDataSet changeUserInfo(long userId, UserDataSet changesForUser) {
        return userDAO.changeUserInfo(userId, changesForUser);
    }

    @Override
    public int deleteUser(long userId) {
        return userDAO.deleteUser(userId);
    }

}
