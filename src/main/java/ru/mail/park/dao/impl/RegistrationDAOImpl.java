package ru.mail.park.dao.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import ru.mail.park.dao.RegistrationDAO;
import ru.mail.park.dataSets.UserDataSet;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Created by zac on 21.10.16.
 */

public class RegistrationDAOImpl extends BaseDAOImpl implements RegistrationDAO{

    public RegistrationDAOImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public ResponseEntity registration(String jsonString) {
        final JsonObject jsonObject = new JsonParser().parse(jsonString).getAsJsonObject();
        final String login = jsonObject.get("login").getAsString();
        final String password = jsonObject.get("password").getAsString();
        final String email = jsonObject.get("email").getAsString();
        if (StringUtils.isEmpty(login)
                || StringUtils.isEmpty(password)
                || StringUtils.isEmpty(email)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("{}");
        }

        final UserDataSet user;
        try (Connection connection = dataSource.getConnection()) {
            final String query = "INSERT INTO Users(login, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, login);
                ps.setString(2, password);
                ps.setString(3, email);
                ps.executeUpdate();
                user = new UserDataSet(login, password, email);
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
}
