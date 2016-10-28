package ru.mail.park.responses;

/**
 * Created by zac on 27.10.16.
 */
public class IdReply {
    private Long userId;

    public IdReply() {
        
    }

    public IdReply(Long userId) {
        this.userId = userId;
    }

    @SuppressWarnings("unused")
    public Long getUserId() {
        return userId;
    }

    @SuppressWarnings("unused")
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
