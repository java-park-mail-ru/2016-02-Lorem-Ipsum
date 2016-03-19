package main;

import fakeclasses.FakeAccountService;
import fakeclasses.FakeSession;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

public class AccountServiceGettersTest {

    private AccountService accountService;
    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;
    private Set<Long> fakeUsersIds;

    private static final long ID_OF_NOT_EXISTING_USER = -1;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email@x.y";
    private UserProfile userProfileOfNotExistingUser = new UserProfile(ID_OF_NOT_EXISTING_USER, LOGIN_OF_NOT_EXISTING_USER,
                                                                PASSWORD_OF_NOT_EXISTING_USER, EMAIL_OF_NOT_EXISTING_USER);

    private static final String NOT_EXISTING_SESSION = "session";

    @Before
    public void init() {
        accountService = new AccountService();
        fakeAccountService = TestGenerator.generateFakeAccountService();
        fakeUsersIds = fakeAccountService.getUsersIds();
        for (Long uId : fakeUsersIds) {
            accountService.addUser(fakeAccountService.getUserById(uId));
        }
        fakeSessionIds = fakeAccountService.getSessionsIds();
        for (String sId : fakeSessionIds) {
            accountService.addSession(sId, fakeAccountService.getSession(sId));
        }
    }

    @Test
    public void testCheckUserExistsByLogin() throws Exception {
        for (Long uId : fakeUsersIds) {
            String login = fakeAccountService.getUserById(uId).getLogin();
            assertTrue("Login from fakeAccountService must be in accountService", accountService.checkUserExistsByLogin(login));
        }
        assertFalse(accountService.checkUserExistsByLogin(LOGIN_OF_NOT_EXISTING_USER));
    }

    @Test
    public void testCheckSessionExists() throws Exception {
        for (String sId : fakeSessionIds) {
            assertTrue(accountService.checkSessionExists(sId));
        }
        assertFalse(accountService.checkSessionExists(NOT_EXISTING_SESSION));
    }

    @Test
    public void testCheckUserExistsById() throws Exception {
        for (Long uId : fakeUsersIds) {
            assertTrue(accountService.checkUserExistsById(uId));
        }
        assertFalse(accountService.checkUserExistsById(ID_OF_NOT_EXISTING_USER));
    }

    @Test
    public void testGetUserByLogin() throws Exception {
        for (Long uId : fakeUsersIds) {
            UserProfile userFromAccountService = accountService.getUserByLogin(fakeAccountService.getUserById(uId).getLogin());
            UserProfile userFromFakeAccountService = fakeAccountService.getUserById(uId);
            assertTrue(userFromAccountService.equal(userFromFakeAccountService));
        }
    }

    @Test
    public void testGetUserById() throws Exception {
        for (Long uId : fakeUsersIds) {
            UserProfile userFromAccountService = accountService.getUserById(uId);
            UserProfile userFromFakeAccountService = fakeAccountService.getUserById(uId);
            assertTrue(userFromAccountService.equal(userFromFakeAccountService));
        }
    }

    @Test
    public void testGetSession() throws Exception {
        for (String sId : fakeSessionIds) {
            UserProfile userFromAccountService = accountService.getSession(sId);
            UserProfile userFromFakeAccountService = fakeAccountService.getSession(sId);
            assertTrue(userFromAccountService.equal(userFromFakeAccountService));
        }
    }

}