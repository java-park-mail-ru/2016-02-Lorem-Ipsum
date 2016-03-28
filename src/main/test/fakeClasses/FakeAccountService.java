package fakeclasses;

import java.lang.String;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import main.IAccountService;
import main.UserProfile;


public class FakeAccountService implements IAccountService {

    private final ConcurrentMap<String, Long> usersIDs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, UserProfile> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserProfile> sessions = new ConcurrentHashMap<>();

    @Override
    public boolean checkUserExistsByLogin(String userLogin){
        return usersIDs.containsKey(userLogin);
    }

    @Override
    public boolean checkSessionExists(String sessionId) {
        return sessions.containsKey(sessionId);
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        return users.containsKey(userId);
    }

    @Override
    public long addUser(UserProfile userProfile) {
        usersIDs.put(userProfile.getLogin(), userProfile.getUserId());
        users.put(userProfile.getUserId(), userProfile);
        return -1;
    }

    @Override
    public void deleteUser(String sessionId, UserProfile userProfile) {
        sessions.remove(sessionId);
        usersIDs.remove(userProfile.getLogin());
        users.remove(userProfile.getUserId());
    }

    @Override
    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    @Override
    public void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
    }

    @Override
    public UserProfile getUserByLogin(String userLogin) {
        return users.get(usersIDs.get(userLogin));
    }

    @Override
    public UserProfile getUserById(Long userId) {
        return users.get(userId);
    }

    @Override
    public UserProfile getSession(String sessionId) {
        return sessions.get(sessionId);
    }

    public Set<Long> getUsersIds() {return users.keySet();}

    public Set<String> getSessionsIds() {return sessions.keySet();}

    @Override
    public void changeUser(UserProfile userProfile) { users.put(userProfile.getUserId(), userProfile); }

}
