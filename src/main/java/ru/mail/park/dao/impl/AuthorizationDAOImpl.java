package ru.mail.park.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.data.SessionDataSet;

import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by zac on 21.10.16.
 */

public class AuthorizationDAOImpl extends BaseDAOImpl implements AuthorizationDAO {

    public AuthorizationDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity login(String jsonString, HttpSession httpSession) {
        final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        final String login = jsonObject.get("login").getAsString();
        final String password = jsonObject.get("password").getAsString();
        if (StringUtils.isEmpty(login) || StringUtils.isEmpty(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        final SessionDataSet session;
        try (Connection connection = dataSource.getConnection()) {
            String query = "SELECT * FROM Users WHERE login = ? AND password = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, login);
                ps.setString(2, password);
                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                    session = new SessionDataSet(httpSession.getId(), resultSet.getLong("id"));
                }
            }
            query = "INSERT INTO Sessions(sessionId, userId) VALUES (?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, session.getSessionId());
                ps.setLong(2, session.getUserId());
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{}");
        }

        return ResponseEntity.ok(session);
    }

    @Override
    public ResponseEntity authorizationCheck(HttpSession httpSession) {
        final SessionDataSet session;
        try (Connection connection = dataSource.getConnection()) {
            final String sessionId = httpSession.getId();
            final String query = "SELECT * FROM Sessions WHERE sessionId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, sessionId);
                try (ResultSet resultSet = ps.executeQuery()) {
                    resultSet.next();
                    session = new SessionDataSet(sessionId, resultSet.getLong("userId"));
                }
            }
        } catch (SQLException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{}");
        }

        return ResponseEntity.ok(session);
    }

    @Override
    public ResponseEntity logout(HttpSession httpSession) {
        try (Connection connection = dataSource.getConnection()) {
            final String sessionId = httpSession.getId();
            final String query = "DELETE FROM Sessions WHERE sessionId = ?";
            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setString(1, sessionId);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            return ResponseEntity.ok("{}");
        }

        return ResponseEntity.ok("{}");
    }

}
