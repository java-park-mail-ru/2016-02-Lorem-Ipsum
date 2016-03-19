package main;

import org.junit.Test;

import static org.junit.Assert.*;


public class UserProfileTest {
    private static final long ORIGINAL_ID = 0;
    private static final String ORIGINAL_LOGIN = "login0";
    private static final String ORIGINAL_PASSWORD = "password0";
    private static final String ORIGINAL_EMAIL = "email0@x.y";
    private static final long NEW_ID = 1;
    private static final String NEW_LOGIN = "login1";
    private static final String NEW_PASSWORD = "password1";
    private static final String NEW_EMAIL = "email1@x.y";
    private UserProfile userProfile0 = new UserProfile(ORIGINAL_ID, ORIGINAL_LOGIN,
                                                        ORIGINAL_PASSWORD, ORIGINAL_EMAIL);
    private UserProfile userProfile1 = new UserProfile(ORIGINAL_ID, ORIGINAL_LOGIN,
            ORIGINAL_PASSWORD, ORIGINAL_EMAIL);

    @Test
    public void testGetId() throws Exception {
        assertEquals(ORIGINAL_ID, userProfile0.getId());
    }

    @Test
    public void testGetLogin() throws Exception {
        assertEquals(ORIGINAL_LOGIN, userProfile0.getLogin());
    }

    @Test
    public void testGetPassword() throws Exception {
        assertEquals(ORIGINAL_PASSWORD, userProfile0.getPassword());
    }

    @Test
    public void testGetEmail() throws Exception {
        assertEquals(ORIGINAL_EMAIL, userProfile0.getEmail());
    }

    @Test
    public void testSetLogin() throws Exception {
        userProfile1.setLogin(NEW_LOGIN);
        assertEquals(NEW_LOGIN, userProfile1.getLogin());
    }

    @Test
    public void testSetPassword() throws Exception {
        userProfile1.setPassword(NEW_PASSWORD);
        assertEquals(NEW_PASSWORD, userProfile1.getPassword());
    }

    @Test
    public void testSetEmail() throws Exception {
        userProfile1.setEmail(NEW_EMAIL);
        assertEquals(NEW_EMAIL, userProfile1.getEmail());
    }
}