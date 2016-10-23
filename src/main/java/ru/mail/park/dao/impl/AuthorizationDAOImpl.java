package ru.mail.park.dao.impl;

import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.data.SessionDataSet;
import ru.mail.park.data.UserDataSet;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zac on 21.10.16.
 */

@Repository
public class AuthorizationDAOImpl implements AuthorizationDAO {
    protected DataSource dataSource;

    @Autowired
    public AuthorizationDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Nullable
    @Override
    public SessionDataSet login(UserDataSet user, String sessionId) {
        final SessionDataSet session;
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM Users WHERE login = ? AND password = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, user.getLogin());
                ps.setString(2, user.getPassword());
                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                    session = new SessionDataSet(sessionId, resultSet.getLong("id"));
                }
            }
            query = "INSERT INTO Sessions(sessionId, userId) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, session.getSessionId());
                ps.setLong(2, session.getUserId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return null;
        }

        return session;
    }

    @Nullable
    @Override
    public SessionDataSet authorizationCheck(String sessionId) {
        final SessionDataSet session;
        try (Connection connection = dataSource.getConnection()) {
            final String query = "SELECT * FROM Sessions WHERE sessionId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, sessionId);
                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                    session = new SessionDataSet(sessionId, resultSet.getLong("userId"));
                }
            }
        } catch (SQLException e) {
            return null;
        }

        return session;
    }

    @Override
    public void logout(String sessionId) {
        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM Sessions WHERE sessionId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, sessionId);
                ps.executeUpdate();
            }
        } catch (SQLException ignored) {

        }
    }

}
