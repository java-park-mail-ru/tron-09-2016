package ru.mail.park.main;

/**
 * Created by zac on 06.10.16.
 */

public class Helper {

    public Helper() {
    }

    public static String getIdResponse(long id) {
        return "{\n" + "  \"id\": \"" + Long.toString(id) + "\"\n" + '}';
    }

}
