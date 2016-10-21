package ru.mail.park.services;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.model.UserDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class SessionService {
    private Map<String, UserDataSet> sessionIdToUser = new HashMap<>();

    public void addSession(String sessionId, UserDataSet user) {
        sessionIdToUser.put(sessionId, user);
    }

    @Nullable
    public UserDataSet getUser(String sessionId) {
        if (sessionIdToUser.containsKey(sessionId))
            return sessionIdToUser.get(sessionId);

        return null;
    }

    public void deleteSession(String sessionId) {
        sessionIdToUser.remove(sessionId);
    }

    public void deleteAllSessions(String login){
        ArrayList<String> userSessions = new ArrayList<>();

        for(Map.Entry<String, UserDataSet> entry : sessionIdToUser.entrySet()) {
            final UserDataSet user = entry.getValue();

            if (login.equals(user.getLogin())) {
                userSessions.add(entry.getKey());
            }
        }

        for(String sessionId: userSessions) {
            sessionIdToUser.remove(sessionId);
        }
    }

}