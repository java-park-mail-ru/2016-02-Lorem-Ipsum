package main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Installed on 13.03.2016.
 */
public class AccountServiceAddTest {

    private AccountService accountService;

    private static final long ID_OF_NOT_EXISTING_USER = 0;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login0";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password0";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email0@x.y";

    private static final long ID_OF_NOT_AUTHORIZED_USER = 1;
    private static final String LOGIN_OF_NOT_AUTHORIZED_USER = "login1";
    private static final String PASSWORD_OF_NOT_AUTHORIZED_USER = "password1";
    private static final String EMAIL_OF_NOT_AUTHORIZED_USER = "email1@x.y";
    private final UserProfile userProfileOfNotAuthorizedUser = new UserProfile(ID_OF_NOT_AUTHORIZED_USER, LOGIN_OF_NOT_AUTHORIZED_USER,
            PASSWORD_OF_NOT_AUTHORIZED_USER, EMAIL_OF_NOT_AUTHORIZED_USER);

    private static final long ID_OF_AUTHORIZED_USER = 2;
    private static final String LOGIN_OF_AUTHORIZED_USER = "login2";
    private static final String PASSWORD_OF_AUTHORIZED_USER = "password2";
    private static final String EMAIL_OF_AUTHORIZED_USER = "email2@x.y";
    private static final String SESSION_OF_AUTHORIZED_USER = "session2";
    private final UserProfile userProfileOfAuthorizedUser = new UserProfile(ID_OF_AUTHORIZED_USER, LOGIN_OF_AUTHORIZED_USER,
            PASSWORD_OF_AUTHORIZED_USER, EMAIL_OF_AUTHORIZED_USER);

    @Before
    public void init() {
        accountService = new AccountService();
    }

    @Test
    public void testAddUser() throws Exception {
        accountService.addUser(userProfileOfNotAuthorizedUser);
        accountService.addUser(userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(ID_OF_NOT_AUTHORIZED_USER));
        assertTrue(accountService.checkUserExistsById(ID_OF_AUTHORIZED_USER));
        assertTrue(accountService.getUserById(ID_OF_NOT_AUTHORIZED_USER).equal(userProfileOfNotAuthorizedUser));
        assertTrue(accountService.getUserById(ID_OF_AUTHORIZED_USER).equal(userProfileOfAuthorizedUser));
    }

    @Test
    public void testAddSession() throws Exception {
        accountService.addUser(userProfileOfAuthorizedUser);
        accountService.addSession(SESSION_OF_AUTHORIZED_USER, userProfileOfAuthorizedUser);
        assertTrue(accountService.checkUserExistsById(ID_OF_AUTHORIZED_USER));
        assertTrue(accountService.checkSessionExists(SESSION_OF_AUTHORIZED_USER));
    }
}