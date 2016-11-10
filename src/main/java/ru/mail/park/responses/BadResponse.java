package ru.mail.park.responses;

/**
 * Created by zac on 22.10.16.
 */

public class BadResponse {
    private int status;
    private String message;

    public BadResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    @SuppressWarnings("unused")
    public int getStatus() {
        return status;
    }

    @SuppressWarnings("unused")
    public void setStatus(int status) {
        this.status = status;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public void setMessage(String message) {
        this.message = message;
    }
}
