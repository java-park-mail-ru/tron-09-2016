package ru.mail.park.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mail.park.dao.RegistrationDAO;
import ru.mail.park.dao.impl.RegistrationDAOImpl;

import javax.sql.DataSource;

/**
 * Created by Zac on 01/10/16.
 */

@RestController
@RequestMapping(value = "/api/user")
public class RegistrationController extends BaseController {
    private RegistrationDAO registrationDAO;

    public RegistrationController(DataSource dataSource) {
        super(dataSource);
    }

    @Override
    void init() {
        super.init();
        registrationDAO = new RegistrationDAOImpl(dataSource);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity registration(@RequestBody String body){
        return registrationDAO.registration(body);
    }
}
