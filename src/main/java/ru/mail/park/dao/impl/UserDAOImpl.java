package ru.mail.park.dao.impl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.mail.park.dao.AuthorizationDAO;
import ru.mail.park.dao.UserDAO;
import ru.mail.park.dataSets.UserDataSet;

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

    public UserDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity getUserInfo(long userId, HttpSession httpSession) {
        final AuthorizationDAO authorizationDAO = new AuthorizationDAOImpl(dataSource);
        final ResponseEntity responseEntity = authorizationDAO.authorizationCheck(httpSession);
        if (responseEntity.getStatusCode() == HttpStatus.UNAUTHORIZED) {
            System.out.println("Не авторизован");
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
}
