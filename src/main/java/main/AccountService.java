package main;

import java.lang.String;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService {

    private final ConcurrentMap<String, Long> usersIDs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, UserProfile> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserProfile> sessions = new ConcurrentHashMap<>();

    public boolean checkUserExistsByLogin(String userLogin){
        return usersIDs.containsKey(userLogin);
    }

    public boolean checkSessionExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    public boolean checkUserExistsById(Long userId) {
        return users.containsKey(userId);
    }

    public void addUser(UserProfile userProfile) {
        usersIDs.put(userProfile.getLogin(), userProfile.getId());
        users.put(userProfile.getId(), userProfile);
    }

    public void deleteUser(String sessionId, UserProfile userProfile) {
        sessions.remove(sessionId);
        usersIDs.remove(userProfile.getLogin());
        users.remove(userProfile.getId());
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void addSessions(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public UserProfile getUserByLogin(String userLogin) {
        return users.get(usersIDs.get(userLogin));
    }

    public UserProfile getUserById(Long userId) {
        return users.get(userId);
    }

    public UserProfile getSessions(String sessionId) {
        return sessions.get(sessionId);
    }


}
