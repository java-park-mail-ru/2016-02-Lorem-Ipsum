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
public class IsAuthenticatedServletTest {

    private FakeAccountService fakeAccountService;
    private Set<String> fakeSessionIds;
    private IsAuthenticatedServlet isAuthenticatedServlet;

    public static final Logger LOGGER = LogManager.getLogger("TestLogger");

    @Before
    public void init() {
        fakeAccountService = TestGenerator.generateFakeAccountService();
        isAuthenticatedServlet = new IsAuthenticatedServlet(fakeAccountService);
        Set<Long> fakeUsersIds = fakeAccountService.getUsersIds();
        fakeSessionIds = fakeAccountService.getSessionsIds();
        LOGGER.info("IsAuthenticatedServletTest inited.");
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
                    IsAuthenticatedServlet.REQUEST_URI
            );

            LOGGER.info("Created request: {}", request.toJSON());

            FakeResponseImpl response = new FakeResponseImpl();

            isAuthenticatedServlet.doGet(request, response);

            assertTrue(fakeAccountService.checkSessionExists(sId));
            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            String stringToLog = responseContentStr.replace("\r\n", "");
            LOGGER.info("Got response: {}", stringToLog);
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            Number id = (Number) responseContentJSON.get("id");
            assertTrue(id.longValue() == userProfileGenerated.getUserId());
        }
    }
}