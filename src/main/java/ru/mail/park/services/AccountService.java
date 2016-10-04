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
        if (userNameToUser.containsKey(login))
            return userNameToUser.get(login);

        return null;
    }

    public boolean isEmailFree(String email) {
        for(Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            final UserProfile user = entry.getValue();

            if (user.getEmail().equals(email)){
                return false;
            }
        }

        return true;
    }

    public boolean isLoginFree(String login) {
        for(Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            final UserProfile user = entry.getValue();

            if (user.getLogin().equals(login)){
                return false;
            }
        }

        return true;
    }

    public void deletePairByKey(String login) {
        if (userNameToUser.containsKey(login))
            userNameToUser.remove(login);
    }

}
