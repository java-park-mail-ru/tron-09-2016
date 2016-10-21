package ru.mail.park.dataSets;

/**
 * Created by zac on 21.10.16.
 */

public class SessionDataSet {
    private String sessionId;
    private String userId;

    public SessionDataSet(String sessionId, String userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
