package main;

import database.exceptions.sesexceptions.AddSessionException;
import database.exceptions.sesexceptions.DeleteSessionException;
import database.exceptions.sesexceptions.GetSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.*;

/**
 * Created by Installed on 19.03.2016.
 */
public interface IAccountService {
    boolean checkUserExistsByLogin(String userLogin) throws UserExistsException;

    boolean checkSessionExists(String sessionId) throws SessionExistsException;

    boolean checkUserExistsById(Long userId) throws UserExistsException;

    long addUser(UserProfile userProfile) throws AddUserException;

    void deleteUser(String sessionId, UserProfile userProfile) throws DeleteUserException;

    void changeUser(UserProfile userProfile) throws ChangeUserException;

    void deleteSession(String sessionId) throws DeleteSessionException;

    void addSession(String sessionId, UserProfile userProfile) throws AddSessionException;

    UserProfile getUserByLogin(String userLogin) throws GetUserException;

    UserProfile getUserById(Long userId) throws GetUserException;

    UserProfile getSession(String sessionId) throws GetSessionException;
}
