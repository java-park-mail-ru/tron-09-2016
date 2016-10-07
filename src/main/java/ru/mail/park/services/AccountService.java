package ru.mail.park.services;

import org.jetbrains.annotations.Nullable;
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
        return userNameToUser.get(login);
    }

    @Nullable
    public UserProfile getUserBYId(long id) {
        for(Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            final UserProfile user = entry.getValue();

            if (user.getID() == id) {
                return user;
            }
        }

        return null;
    }

    public boolean isEmailFree(String email) {
        for(Map.Entry<String, UserProfile> entry : userNameToUser.entrySet()) {
            final UserProfile user = entry.getValue();

            if (user.getEmail().equals(email)) {
                return false;
            }
        }

        return true;
    }

    public void deleteUser(String login) {
        userNameToUser.remove(login);
    }

}
