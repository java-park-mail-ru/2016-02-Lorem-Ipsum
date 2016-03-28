package main;

import database.DbService;
import database.IDbService;
import fakeclasses.FakeAccountService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

@SuppressWarnings("FieldCanBeLocal")
public class AccountServiceDelTest {

    private IDbService accountService;
    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;
    private Set<Long> fakeUsersIds;
    private Map<Long, Long> generatedIds;

    private static final long ID_OF_NOT_EXISTING_USER = -1;
    private static final String LOGIN_OF_NOT_EXISTING_USER = "login";
    private static final String PASSWORD_OF_NOT_EXISTING_USER = "password";
    private static final String EMAIL_OF_NOT_EXISTING_USER = "email@x.y";

    private static final String NOT_EXISTING_SESSION = "session";

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
        fakeAccountService = TestGenerator.generateFakeAccountService();
        fakeUsersIds = fakeAccountService.getUsersIds();
        generatedIds = new HashMap<>();
        for (Long uId : fakeUsersIds) {
            UserProfile userToAdd = fakeAccountService.getUserById(uId);
            Long generatedId = accountService.addUser(userToAdd);
            generatedIds.put(uId, generatedId);
        }
        fakeSessionIds = fakeAccountService.getSessionsIds();
        for (String sId : fakeSessionIds) {
            UserProfile userFake = fakeAccountService.getSession(sId);
            UserProfile userToAdd = userFake.clone();
            userToAdd.setUserId(generatedIds.get(userFake.getUserId()));
            accountService.addSession(sId, userToAdd);
        }
    }

    @Test
    public void testDeleteUser() throws Exception {
        for (String sId : fakeSessionIds) {
            UserProfile userFake = fakeAccountService.getSession(sId);
            UserProfile userToDelete = userFake.clone();
            userToDelete.setUserId(generatedIds.get(userFake.getUserId()));
            accountService.deleteUser(sId, userToDelete);
            assertFalse(accountService.checkSessionExists(sId));
            assertFalse(accountService.checkUserExistsById(userToDelete.getUserId()));
        }
        assertFalse(accountService.checkSessionExists(NOT_EXISTING_SESSION));
        assertFalse(accountService.checkUserExistsById(ID_OF_NOT_EXISTING_USER));
    }

    @Test
    public void testDeleteSession() throws Exception {
        for (String sId : fakeSessionIds) {
            UserProfile user = fakeAccountService.getSession(sId);
            accountService.deleteSession(sId);
            assertFalse(accountService.checkSessionExists(sId));
            assertTrue(accountService.checkUserExistsById(generatedIds.get(user.getUserId())));
        }
        assertFalse(accountService.checkSessionExists(NOT_EXISTING_SESSION));
    }

    @After
    public void close() {
        accountService.close();
    }

}