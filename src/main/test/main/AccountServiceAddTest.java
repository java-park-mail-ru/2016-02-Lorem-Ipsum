package main;

import database.DbService;
import database.IDbService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Installed on 13.03.2016.
 */
public class AccountServiceAddTest {

    private IDbService accountService;

    private static long ID_OF_NOT_EXISTING_USER = 0;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login0";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password0";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email0@x.y";

    private static long ID_OF_NOT_AUTHORIZED_USER = 1;
    private static final String LOGIN_OF_NOT_AUTHORIZED_USER = "login1";
    private static final String PASSWORD_OF_NOT_AUTHORIZED_USER = "password1";
    private static final String EMAIL_OF_NOT_AUTHORIZED_USER = "email1@x.y";
    private static final String SESSION_OF_NOT_AUTHORIZED_USER = "session1";
    private final UserProfile userProfileOfNotAuthorizedUser = new UserProfile(ID_OF_NOT_AUTHORIZED_USER, LOGIN_OF_NOT_AUTHORIZED_USER,
            PASSWORD_OF_NOT_AUTHORIZED_USER, EMAIL_OF_NOT_AUTHORIZED_USER);

    private static long ID_OF_AUTHORIZED_USER = 2;
    private static final String LOGIN_OF_AUTHORIZED_USER = "login2";
    private static final String PASSWORD_OF_AUTHORIZED_USER = "password2";
    private static final String EMAIL_OF_AUTHORIZED_USER = "email2@x.y";
    private static final String SESSION_OF_AUTHORIZED_USER = "session2";
    private final UserProfile userProfileOfAuthorizedUser = new UserProfile(ID_OF_AUTHORIZED_USER, LOGIN_OF_AUTHORIZED_USER,
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
    public void testAddUser() throws Exception {
        ID_OF_NOT_AUTHORIZED_USER = accountService.addUser(userProfileOfNotAuthorizedUser);
        ID_OF_AUTHORIZED_USER = accountService.addUser(userProfileOfAuthorizedUser);
        userProfileOfNotAuthorizedUser.setUserId(ID_OF_NOT_AUTHORIZED_USER);
        userProfileOfAuthorizedUser.setUserId(ID_OF_AUTHORIZED_USER);
        accountService.addSession(SESSION_OF_AUTHORIZED_USER, userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(ID_OF_NOT_AUTHORIZED_USER));
        assertTrue(accountService.checkUserExistsById(ID_OF_AUTHORIZED_USER));
        assertTrue(accountService.getUserById(ID_OF_NOT_AUTHORIZED_USER).equal(userProfileOfNotAuthorizedUser));
        assertTrue(accountService.getUserById(ID_OF_AUTHORIZED_USER).equal(userProfileOfAuthorizedUser));
    }

    @Test
    public void testAddSession() throws Exception {
        ID_OF_AUTHORIZED_USER = accountService.addUser(userProfileOfAuthorizedUser);
        userProfileOfAuthorizedUser.setUserId(ID_OF_AUTHORIZED_USER);
        accountService.addSession(SESSION_OF_AUTHORIZED_USER, userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(ID_OF_AUTHORIZED_USER));
        assertTrue(accountService.checkSessionExists(SESSION_OF_AUTHORIZED_USER));
    }

    @After
    public void close() {
        accountService.close();
    }
}