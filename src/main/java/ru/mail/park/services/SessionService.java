package ru.mail.park.services;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * ДЗ: реализовать класс, который по соответствующему id сессии будет отдавать пользователей(т.е. реализовать авторизацию по сессии)
 *
 */
@Service
public class SessionService {
    private Map<String, UserProfile> sessionIdToUser = new HashMap<>();

    public void addSession(String sessionId, UserProfile user) {
        sessionIdToUser.put(sessionId, user);
    }

    @Nullable
    public UserProfile getUser(String sessionId) {
        if (sessionIdToUser.containsKey(sessionId))
            return sessionIdToUser.get(sessionId);

        return null;
    }

    public void deleteSession(String sessionId) {
        if (sessionIdToUser.containsKey(sessionId))
            sessionIdToUser.remove(sessionId);
    }

    public void deleteAllSessions(String login){
        Vector<String> userSessions = new Vector<>();

        for(Map.Entry<String, UserProfile> entry : sessionIdToUser.entrySet()) {
            final UserProfile user = entry.getValue();

            if (login.equals(user.getLogin())) {
                userSessions.add(entry.getKey());
            }
        }

        for(String sessionId: userSessions) {
            sessionIdToUser.remove(sessionId);
        }
    }
}