package frontend;

import fakeclasses.FakeAccountService;
import fakeclasses.FakeRequestImpl;
import fakeclasses.FakeResponseImpl;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Installed on 12.03.2016.
 */
@SuppressWarnings("UnusedAssignment")
public class DeleteUserServletTest {

    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;
    private DeleteUserServlet deleteUserServlet;

    public static final Logger LOGGER = LogManager.getLogger("TestLogger");

    @Before
    public void init() {
        fakeAccountService = TestGenerator.generateFakeAccountService();
        deleteUserServlet = new DeleteUserServlet(fakeAccountService);
        Set<Long> fakeUsersIds = fakeAccountService.getUsersIds();
        fakeSessionIds = fakeAccountService.getSessionsIds();
        LOGGER.info("DeleteUserServletTest inited.");
    }

    @Test
    public void testDoDelete() throws ServletException, IOException, JSONException {
        for (String sId : fakeSessionIds) {

            UserProfile userProfileGenerated = fakeAccountService.getSession(sId);

            FakeRequestImpl request = new FakeRequestImpl(
                    userProfileGenerated.getLogin(),
                    userProfileGenerated.getPassword(),
                    userProfileGenerated.getEmail(),
                    sId,
                    DeleteUserServlet.REQUEST_URI + userProfileGenerated.getUserId()
            );

            LOGGER.info("Created request: {}", request.toJSON());

            FakeResponseImpl response = new FakeResponseImpl();

            deleteUserServlet.doDelete(request, response);

            assertFalse(fakeAccountService.checkSessionExists(sId));
            assertFalse(fakeAccountService.checkUserExistsById(userProfileGenerated.getUserId()));
            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            String stringToLog = responseContentStr.replace("\r\n", "");
            LOGGER.info("Got response: {}", stringToLog);
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            assertTrue(responseContentJSON.keySet().isEmpty());
        }
    }
}