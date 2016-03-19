package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.String;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

public class AccountService implements IAccountService {

    private final ConcurrentMap<String, Long> usersIDs = new ConcurrentHashMap<>();
    private final ConcurrentMap<Long, UserProfile> users = new ConcurrentHashMap<>();
    private final ConcurrentMap<String, UserProfile> sessions = new ConcurrentHashMap<>();

    public static final Logger LOGGER = LogManager.getLogger(AccountService.class);

    @Override
    public boolean checkUserExistsByLogin(String userLogin){
        LOGGER.debug("Checked existance of user with login {}", userLogin);
        return usersIDs.containsKey(userLogin);
    }

    @Override
    public boolean checkSessionExists(String sessionId) {
        LOGGER.debug("Checked existance of session with {}", sessionId);
        return sessions.containsKey(sessionId);
    }

    @Override
    public boolean checkUserExistsById(Long userId) {
        LOGGER.debug("Checked existance of user with id {}", userId);
        return users.containsKey(userId);
    }

    @Override
    public void addUser(UserProfile userProfile) {
        usersIDs.put(userProfile.getLogin(), userProfile.getId());
        users.put(userProfile.getId(), userProfile);
        LOGGER.debug("User was added: {}", userProfile.toJSON());
    }

    @Override
    public void deleteUser(String sessionId, UserProfile userProfile) {
        sessions.remove(sessionId);
        usersIDs.remove(userProfile.getLogin());
        users.remove(userProfile.getId());
        LOGGER.debug("User with session {} was deleted: {}", sessionId, userProfile.toJSON());
    }

    @Override
    public void deleteSession(String sessionId) {
        sessions.remove(sessionId);
        LOGGER.debug("Session deleted, id {}", sessionId);
    }

    @Override
    public void addSession(String sessionId, UserProfile userProfile) {
        sessions.put(sessionId, userProfile);
        LOGGER.debug("User with session {} was added: {}", sessionId, userProfile.toJSON());
    }

    @Override
    public UserProfile getUserByLogin(String userLogin) {
        LOGGER.debug("User got by login: {}", userLogin);
        return users.get(usersIDs.get(userLogin));
    }

    @Override
    public UserProfile getUserById(Long userId) {
        LOGGER.debug("User got by id: {}", userId);
        return users.get(userId);
    }

    @Override
    public UserProfile getSession(String sessionId) {
        LOGGER.debug("Session got by id: {}", sessionId);
        return sessions.get(sessionId);
    }


}
