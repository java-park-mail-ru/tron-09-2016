package ru.mail.park.services;

import org.springframework.stereotype.Service;
import ru.mail.park.model.UserProfile;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private Map<String, UserProfile> userNameToUser = new HashMap<>();

    public void addUser(UserProfile userProfile) {
        userNameToUser.put(userProfile.getLogin(), userProfile);
    }

    public UserProfile getUser(String login) {
        if (!userNameToUser.containsKey(login))
            return null;

        return userNameToUser.get(login);
    }
}
