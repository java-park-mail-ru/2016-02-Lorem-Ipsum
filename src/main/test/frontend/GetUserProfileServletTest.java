package frontend;

import fakeclasses.FakeAccountService;
import fakeclasses.FakeRequestImpl;
import fakeclasses.FakeResponseImpl;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Installed on 12.03.2016.
 */
public class GetUserProfileServletTest {

    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;
    private GetUserProfileServlet getUserProfileServlet;

    public static final Logger LOGGER = LogManager.getLogger("TestLogger");

    @Before
    public void init() {
        fakeAccountService = TestGenerator.generateFakeAccountService();
        getUserProfileServlet = new GetUserProfileServlet(fakeAccountService);
        Set<Long> fakeUsersIds = fakeAccountService.getUsersIds();
        fakeSessionIds = fakeAccountService.getSessionsIds();
        LOGGER.info("GetUserProfileServletTest inited.");
    }

    @Test
    public void testDoGet() throws Exception {
        for (String sId : fakeSessionIds) {

            UserProfile userProfileGenerated = fakeAccountService.getSession(sId);

            FakeRequestImpl request = new FakeRequestImpl(
                    userProfileGenerated.getLogin(),
                    userProfileGenerated.getPassword(),
                    userProfileGenerated.getEmail(),
                    sId,
                    GetUserProfileServlet.REQUEST_URI + userProfileGenerated.getUserId()
            );

            LOGGER.info("Created request: {}", request.toJSON());

            FakeResponseImpl response = new FakeResponseImpl();

            getUserProfileServlet.doGet(request, response);

            assertTrue(fakeAccountService.checkSessionExists(sId));
            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            String stringToLog = responseContentStr.replace("\r\n", "");
            LOGGER.info("Got response: {}", stringToLog);
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            Number id = (Number) responseContentJSON.get("id");
            String login = (String)responseContentJSON.get("login");
            String email = (String)responseContentJSON.get("email");
            assertEquals(id.longValue(), userProfileGenerated.getUserId());
            assertEquals(login, userProfileGenerated.getLogin());
            assertEquals(email, userProfileGenerated.getEmail());
        }
    }
}