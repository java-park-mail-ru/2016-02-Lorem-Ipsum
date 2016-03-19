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

public class ChangeUserServletTest {

    private FakeAccountService fakeAccountServiceToGenerate;
    private Set<String> fakeSessionIdsOfGenerated;
    private ChangeUserServlet changeUserServlet;

    public static final Logger LOGGER = LogManager.getLogger("TestLogger");

    @Before
    public void init() {
        fakeAccountServiceToGenerate = TestGenerator.generateFakeAccountService();
        FakeAccountService fakeAccountServiceToPutChanged = new FakeAccountService();
        changeUserServlet = new ChangeUserServlet(fakeAccountServiceToGenerate);
        Set<Long> fakeUsersIdsOfGenerated = fakeAccountServiceToGenerate.getUsersIds();
        fakeSessionIdsOfGenerated = fakeAccountServiceToGenerate.getSessionsIds();
        LOGGER.info("ChangeUserServletTest inited.");
    }

    @Test
    public void testDoPost() throws Exception {
        for (String sId : fakeSessionIdsOfGenerated) {

            UserProfile userProfileGenerated = fakeAccountServiceToGenerate.getSession(sId);

            String newLogin = userProfileGenerated.getLogin() + "new";
            String newPassword = userProfileGenerated.getPassword() + "new";
            String newEmail = userProfileGenerated.getEmail() + "new";

            FakeRequestImpl request = new FakeRequestImpl(
                    newLogin,
                    newPassword,
                    newEmail,
                    sId,
                    ChangeUserServlet.REQUEST_URI + userProfileGenerated.getId()
            );

            LOGGER.info("Created request: {}", request.toJSON());

            FakeResponseImpl response = new FakeResponseImpl();

            changeUserServlet.doPost(request, response);

            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            String stringToLog = responseContentStr.replace("\r\n", "");
            LOGGER.info("Got response: {}", stringToLog);
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            Number newId = (Number) responseContentJSON.get("id");
            UserProfile userProfileChanged = fakeAccountServiceToGenerate.getUserById(newId.longValue());
            assertEquals(newLogin, userProfileChanged.getLogin());
            assertEquals(newPassword, userProfileChanged.getPassword());
            assertEquals(newEmail, userProfileChanged.getEmail());
        }
    }
}