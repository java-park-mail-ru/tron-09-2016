package ru.mail.park.controllers;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

/**
 * Created by zac on 21.10.16.
 */

public class BaseController {
    protected final DataSource dataSource;

    @Autowired
    public BaseController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @PostConstruct
    void init() {

    }
}
