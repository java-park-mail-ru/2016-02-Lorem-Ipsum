package main;

import database.DbService;
import database.IDbService;
import database.exceptions.sesexceptions.AddSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.AddUserException;
import database.exceptions.userexceptions.GetUserException;
import database.exceptions.userexceptions.UserExistsException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Installed on 13.03.2016.
 */
public class AccountServiceAddTest {

    private IDbService accountService;

    private static long idOfNotExistingUser = 0;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login0";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password0";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email0@x.y";

    private static long idOfNotAuthorizedUser = 1;
    private static final String LOGIN_OF_NOT_AUTHORIZED_USER = "login1";
    private static final String PASSWORD_OF_NOT_AUTHORIZED_USER = "password1";
    private static final String EMAIL_OF_NOT_AUTHORIZED_USER = "email1@x.y";
    private static final String SESSION_OF_NOT_AUTHORIZED_USER = "session1";
    private final UserProfile userProfileOfNotAuthorizedUser = new UserProfile(idOfNotAuthorizedUser, LOGIN_OF_NOT_AUTHORIZED_USER,
            PASSWORD_OF_NOT_AUTHORIZED_USER, EMAIL_OF_NOT_AUTHORIZED_USER);

    private static long idOfAuthorizedUser = 2;
    private static final String LOGIN_OF_AUTHORIZED_USER = "login2";
    private static final String PASSWORD_OF_AUTHORIZED_USER = "password2";
    private static final String EMAIL_OF_AUTHORIZED_USER = "email2@x.y";
    private static final String SESSION_OF_AUTHORIZED_USER = "session2";
    private final UserProfile userProfileOfAuthorizedUser = new UserProfile(idOfAuthorizedUser, LOGIN_OF_AUTHORIZED_USER,
            PASSWORD_OF_AUTHORIZED_USER, EMAIL_OF_AUTHORIZED_USER);

    @Before
    public void init() {

        accountService = new DbService(
                Main.STANDART_MYSQL_HOST,
                Main.STANDART_MYSQL_PORT,
                Main.STANDART_MYSQL_DB_NAME,
                Main.STANDART_MYSQL_DRIVER,
                Main.STANDART_MYSQL_LOGIN,
                Main.STANDART_MYSQL_PASSWORD
        );
    }

    @Test
    public void testAddUser() throws AddUserException,
            AddSessionException,
            UserExistsException,
            GetUserException {
        idOfNotAuthorizedUser = accountService.addUser(userProfileOfNotAuthorizedUser);
        idOfAuthorizedUser = accountService.addUser(userProfileOfAuthorizedUser);
        userProfileOfNotAuthorizedUser.setUserId(idOfNotAuthorizedUser);
        userProfileOfAuthorizedUser.setUserId(idOfAuthorizedUser);
        accountService.addSession(SESSION_OF_AUTHORIZED_USER, userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(idOfNotAuthorizedUser));
        assertTrue(accountService.checkUserExistsById(idOfAuthorizedUser));
        assertTrue(accountService.getUserById(idOfNotAuthorizedUser).equal(userProfileOfNotAuthorizedUser));
        assertTrue(accountService.getUserById(idOfAuthorizedUser).equal(userProfileOfAuthorizedUser));
    }

    @Test
    public void testAddSession() throws AddUserException, AddSessionException, UserExistsException, SessionExistsException {
        idOfAuthorizedUser = accountService.addUser(userProfileOfAuthorizedUser);
        userProfileOfAuthorizedUser.setUserId(idOfAuthorizedUser);
        accountService.addSession(SESSION_OF_AUTHORIZED_USER, userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(idOfAuthorizedUser));
        assertTrue(accountService.checkSessionExists(SESSION_OF_AUTHORIZED_USER));
    }

    @After
    public void close() {
        accountService.close();
    }
}