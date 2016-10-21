package ru.mail.park.services;

import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import ru.mail.park.dataSets.UserDataSet;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private Map<String, UserDataSet> userNameToUser = new HashMap<>();

    public void addUser(UserDataSet userDataSet) {
        userNameToUser.put(userDataSet.getLogin(), userDataSet);
    }

    public UserDataSet getUser(String login) {
        return userNameToUser.get(login);
    }

    @Nullable
    public UserDataSet getUserBYId(long id) {
        for(Map.Entry<String, UserDataSet> entry : userNameToUser.entrySet()) {
            final UserDataSet user = entry.getValue();

            if (user.getID() == id) {
                return user;
            }
        }

        return null;
    }

    public boolean isEmailFree(String email) {
        for(Map.Entry<String, UserDataSet> entry : userNameToUser.entrySet()) {
            final UserDataSet user = entry.getValue();

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
