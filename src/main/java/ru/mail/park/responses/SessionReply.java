package ru.mail.park.responses;

/**
 * Created by zac on 21.10.16.
 */

public class SessionReply {
    private String sessionId;
    private Long userId;

    public SessionReply(String sessionId, Long userId) {
        this.sessionId = sessionId;
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public String getSessionId() {
        return sessionId;
    }

    @SuppressWarnings("unused")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @SuppressWarnings("unused")
    public Long getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(long userId) {
        this.userId = userId;
    }
}
