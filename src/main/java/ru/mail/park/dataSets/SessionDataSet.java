package ru.mail.park.dataSets;

import com.google.gson.JsonObject;

/**
 * Created by zac on 21.10.16.
 */

public class SessionDataSet {
    private String sessionId;
    private long userId;

    public SessionDataSet(String sessionId, long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
