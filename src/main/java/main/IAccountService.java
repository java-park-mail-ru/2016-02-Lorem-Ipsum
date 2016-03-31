package main;

/**
 * Created by Installed on 19.03.2016.
 */
public interface IAccountService {
    boolean checkUserExistsByLogin(String userLogin);

    boolean checkSessionExists(String sessionId);

    boolean checkUserExistsById(Long userId);

    long addUser(UserProfile userProfile);

    void deleteUser(String sessionId, UserProfile userProfile);

    void changeUser(UserProfile userProfile);

    void deleteSession(String sessionId);

    void addSession(String sessionId, UserProfile userProfile);

    UserProfile getUserByLogin(String userLogin);

    UserProfile getUserById(Long userId);

    UserProfile getSession(String sessionId);
}
