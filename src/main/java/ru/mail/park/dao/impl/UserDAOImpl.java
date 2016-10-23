package ru.mail.park.dao.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.data.SessionDataSet;
import ru.mail.park.data.UserDataSet;
import ru.mail.park.responses.BadResponse;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by zac on 21.10.16.
 */

public class UserDAOImpl extends BaseDAOImpl implements UserDAO {
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity registration(UserDataSet user) {
        final String login = user.getLogin();
        final String password = user.getPassword();
        final String email = user.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        try (Connection connection = dataSource.getConnection()) {
            final String query = "INSERT INTO Users(login, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
                try (ResultSet resultSet = ps.getGeneratedKeys()) {
                    resultSet.next();
                    user.setId(resultSet.getLong(1));
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity getUserInfo(long userId, HttpSession httpSession) {
        final AuthorizationDAO authorizationDAO = new AuthorizationDAOImpl(dataSource);
        final ResponseEntity responseEntity = authorizationDAO.authorizationCheck(httpSession);
        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        return ResponseEntity.ok(user);
    }

    @Override
    public ResponseEntity changeUserInfo(long userId, UserDataSet changesForUser, HttpSession httpSession) {
        final AuthorizationDAO authorizationDAO = new AuthorizationDAOImpl(dataSource);
        final ResponseEntity responseEntity = authorizationDAO.authorizationCheck(httpSession);
        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = responseEntity.getBody();
        if (object instanceof SessionDataSet) {
            final SessionDataSet session = (SessionDataSet) object;
            if (session.getUserId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
            }
        }

        final String login = changesForUser.getLogin();
        final String password = changesForUser.getPassword();
        final String email = changesForUser.getEmail();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final UserDataSet changedUser;
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
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        return ResponseEntity.ok(changedUser);
    }

    @Override
    public ResponseEntity deleteUser(long userId, HttpSession httpSession) {
        final AuthorizationDAO authorizationDAO = new AuthorizationDAOImpl(dataSource);
        final ResponseEntity responseEntity = authorizationDAO.authorizationCheck(httpSession);
        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final Object object = responseEntity.getBody();
        if (object instanceof SessionDataSet) {
            final SessionDataSet session = (SessionDataSet) object;
            if (session.getUserId() != userId) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
            }
        }

        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM Sessions WHERE userId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        try (Connection connection = dataSource.getConnection()) {
            final String query = "DELETE FROM Users WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setLong(1, userId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        return ResponseEntity.ok("{}");
    }

}
