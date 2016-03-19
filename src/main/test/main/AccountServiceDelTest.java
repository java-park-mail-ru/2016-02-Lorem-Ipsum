package main;

import fakeclasses.FakeAccountService;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import java.util.Set;

import static org.junit.Assert.*;

public class AccountServiceDelTest {

    private AccountService accountService;
    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;

    private static final long ID_OF_NOT_EXISTING_USER = -1;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email@x.y";

    private static final String NOT_EXISTING_SESSION = "session";

    @Before
    public void init() {
        accountService = new AccountService();
        fakeAccountService = TestGenerator.generateFakeAccountService();
        Set<Long> fakeUsersIds = fakeAccountService.getUsersIds();
        for (Long uId : fakeUsersIds) {
            accountService.addUser(fakeAccountService.getUserById(uId));
        }
        fakeSessionIds = fakeAccountService.getSessionsIds();
        for (String sId : fakeSessionIds) {
            accountService.addSession(sId, fakeAccountService.getSession(sId));
        }
    }

    @Test
    public void testDeleteUser() throws Exception {
        for (String sId : fakeSessionIds) {
            UserProfile user = fakeAccountService.getSession(sId);
            accountService.deleteUser(sId, user);
            assertFalse(accountService.checkSessionExists(sId));
            assertFalse(accountService.checkUserExistsById(user.getId()));
        }
        assertFalse(accountService.checkUserExistsById(ID_OF_NOT_EXISTING_USER));
    }

    @Test
    public void testDeleteSession() throws Exception {
        for (String sId : fakeSessionIds) {
            UserProfile user = fakeAccountService.getSession(sId);
            accountService.deleteSession(sId);
            assertFalse(accountService.checkSessionExists(sId));
            assertTrue(accountService.checkUserExistsById(user.getId()));
        }
        assertFalse(accountService.checkSessionExists(NOT_EXISTING_SESSION));
    }
}