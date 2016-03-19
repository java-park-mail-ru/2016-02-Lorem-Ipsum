package frontend;

import fakeclasses.*;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import utils.TestGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Created by Installed on 12.03.2016.
 */
public class SignUpServletTest {

    private FakeAccountService fakeAccountServiceToRegister;
    private FakeAccountService fakeAccountServiceToGenerate;
    private Set<Long> fakeUsersIdsOfRegistered;
    private Set<String> fakeSessionIdsOfRegistered;
    private Set<String> fakeSessionIdsOfGenerated;
    private SignUpServlet signUpServlet;

    public static final Logger LOGGER = LogManager.getLogger("TestLogger");

    @Before
    public void init() {
        fakeAccountServiceToGenerate = TestGenerator.generateFakeAccountService();
        fakeAccountServiceToRegister = new FakeAccountService();
        signUpServlet = new SignUpServlet(fakeAccountServiceToRegister);
        Set<Long> fakeUsersIdsOfGenerated = fakeAccountServiceToGenerate.getUsersIds();
        fakeSessionIdsOfGenerated = fakeAccountServiceToGenerate.getSessionsIds();
        LOGGER.info("SignUpServletTest inited.");
    }

    @Test
    public void testDoPut() throws Exception {

        for (String sId : fakeSessionIdsOfGenerated) {

            UserProfile userProfileGenerated = fakeAccountServiceToGenerate.getSession(sId);
            FakeRequestImpl request = new FakeRequestImpl(
                        userProfileGenerated.getLogin(),
                        userProfileGenerated.getPassword(),
                        userProfileGenerated.getEmail(),
                        sId,
                        SignUpServlet.REQUEST_URI
                    );
            LOGGER.info("Created request: {}", request.toJSON());

            FakeResponseImpl response = new FakeResponseImpl();

            signUpServlet.doPut(request, response);

            UserProfile userProfileRegistered = fakeAccountServiceToRegister.getUserByLogin(userProfileGenerated.getLogin());
            assertTrue(userProfileRegistered.semanticEqual(userProfileGenerated));
            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            String stringToLog = responseContentStr.replace("\r\n", "");
            LOGGER.info("Got response: {}", stringToLog);
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            Number id = (Number) responseContentJSON.get("id");
            assertTrue(id.longValue() == userProfileRegistered.getId());
        }
    }
}