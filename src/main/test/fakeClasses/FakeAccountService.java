package fakeclasses;

import java.lang.String;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import main.IAccountService;
import main.IAccountServiceOld;
import main.UserProfile;


public class FakeAccountService implements IAccountService {

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

    public long addUser(UserProfile userProfile) {
        usersIDs.put(userProfile.getLogin(), userProfile.getUserId());
        users.put(userProfile.getUserId(), userProfile);
        return -1;
    }

    public void deleteUser(String sessionId, UserProfile userProfile) {
        sessions.remove(sessionId);
        usersIDs.remove(userProfile.getLogin());
        users.remove(userProfile.getUserId());
    }

    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    public UserProfile getUserByLogin(String userLogin) {
        return users.get(usersIDs.get(userLogin));
    }

    public UserProfile getUserById(Long userId) {
        return users.get(userId);
    }

    public UserProfile getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public Set<Long> getUsersIds() {return users.keySet();}

    public Set<String> getSessionsIds() {return sessions.keySet();}

    public void changeUser(UserProfile userProfile) { users.put(userProfile.getUserId(), userProfile); }

}
