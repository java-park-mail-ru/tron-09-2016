package ru.mail.park.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by zac on 28.10.16.
 */

@RestController
@RequestMapping(value = "/api/score")
@SuppressWarnings("unused")
public class ScoreController {
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity getUserInfo() {
        return ResponseEntity.ok(
                "\"response\": [\n" +
                        "{\"login\": \"Kirill34\", \"score\": 54},\n" +
                        "{\"login\": \"Oleg\", \"score\": 49},\n" +
                        "{\"login\": \"Winner\", \"score\": 48},\n" +
                        "{\"login\": \"ManBOy\", \"score\": 30},\n" +
                        "{\"login\": \"Kirpich\", \"score\": 27},\n" +
                        "{\"login\": \"Sunset\", \"score\": 4},\n");
    }
}
