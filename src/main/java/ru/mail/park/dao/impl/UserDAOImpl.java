package ru.mail.park.dao.impl;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.Status;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by zac on 21.10.16.
 */

@Repository
public class UserDAOImpl implements UserDAO {
    protected DataSource dataSource;

    @Autowired
    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    @Override
    public UserDataSet registration(UserDataSet user) {
        try (Connection connection = dataSource.getConnection()) {
            final String query = "INSERT INTO Users(login, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getEmail());
                ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()) {
                    resultSet.next();
                    user.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            return null;
        }

        return user;
    }

    @Nullable
    @Override
    public UserDataSet getUserInfo(long userId, String sessionId) {
        final UserDataSet user;
        try (Connection connection = dataSource.getConnection()) {
            final String query = "SELECT * FROM Users WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, userId);
                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                    user = new UserDataSet(resultSet);
                }
            }
        } catch (SQLException e) {
            return null;
        }

        return user;
    }

    @Nullable
    @Override
    public UserDataSet changeUserInfo(long userId, UserDataSet changesForUser, String sessionId) {
        final UserDataSet changedUser;
        final String login = changesForUser.getLogin();
        final String password = changesForUser.getPassword();
        final String email = changesForUser.getEmail();
        try (Connection connection = dataSource.getConnection()) {
            final String query = "UPDATE Users SET login = ?, password = ?, email = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.setLong(4, userId);
                ps.executeUpdate();
                changedUser = new UserDataSet(userId, login, password, email);
            }
        } catch (SQLException e) {
            return null;
        }

        return changedUser;
    }

    @Override
    public int deleteUser(long userId, String sessionId) {
        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM Sessions WHERE userId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return Status.ERROR;
        }

        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM Users WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return Status.ERROR;
        }

        return Status.OK;
    }

}
