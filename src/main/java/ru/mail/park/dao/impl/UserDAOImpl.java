package ru.mail.park.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dataSets.SessionDataSet;
import ru.mail.park.dataSets.UserDataSet;
import ru.mail.park.responses.BadResponse;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zac on 21.10.16.
 */

public class UserDAOImpl extends BaseDAOImpl implements UserDAO {
    private static final BadResponse BAD_RESPONSE = new BadResponse(403, "Another user");

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
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
    public ResponseEntity changeUserInfo(long userId, String jsonString, HttpSession httpSession) {
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

        final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        final String login = jsonObject.get("login").getAsString();
        final String password = jsonObject.get("password").getAsString();
        final String email = jsonObject.get("email").getAsString();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        final UserDataSet user;
        try (Connection connection = dataSource.getConnection()) {
            final String query = "UPDATE Users SET login = ?, password = ?, email = ? WHERE id = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.setLong(4, userId);
                ps.executeUpdate();
                user = new UserDataSet(userId, login, password, email);
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(BAD_RESPONSE);
        }

        return ResponseEntity.ok(user);
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
