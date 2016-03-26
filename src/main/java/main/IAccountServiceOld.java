package main;

/**
 * Created by Installed on 26.03.2016.
 */
public interface IAccountServiceOld {

    boolean checkUserExistsByLogin(String userLogin);

    boolean checkSessionExists(String sessionId);

    boolean checkUserExistsById(Long userId);

    void addUser(UserProfile userProfile);

    void deleteUser(String sessionId, UserProfile userProfile);

    void deleteSession(String sessionId);

    void addSession(String sessionId, UserProfile userProfile);

    UserProfile getUserByLogin(String userLogin);

    UserProfile getUserById(Long userId);

    UserProfile getSession(String sessionId);

}
