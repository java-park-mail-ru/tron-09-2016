package ru.mail.park.dao;

import org.springframework.http.ResponseEntity;

/**
 * Created by zac on 21.10.16.
 */

public interface RegistrationDAO {
    ResponseEntity registration(String jsonString);
}
