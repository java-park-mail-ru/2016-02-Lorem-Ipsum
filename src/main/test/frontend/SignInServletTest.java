package frontend;

import fakeclasses.FakeAccountService;
import fakeclasses.FakeRequestImpl;
import fakeclasses.FakeResponseImpl;
import main.UserProfile;
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
public class SignInServletTest {

    private FakeAccountService fakeAccountService;
    private Set<Long> fakeUsersIds;
    private Set<String> fakeSessionIds;
    private SignInServlet signInServlet;

    @Before
    public void init() {
        fakeAccountService = TestGenerator.generateFakeAccountService();
        signInServlet = new SignInServlet(fakeAccountService);
        fakeUsersIds = fakeAccountService.getUsersIds();
        fakeSessionIds = fakeAccountService.getSessionsIds();
    }

    @Test
    public void testDoPut() throws Exception {
        for (String sId : fakeSessionIds) {

            UserProfile userProfileGenerated = fakeAccountService.getSession(sId);

            FakeRequestImpl request = new FakeRequestImpl(
                    userProfileGenerated.getLogin(),
                    userProfileGenerated.getPassword(),
                    userProfileGenerated.getEmail(),
                    sId,
                    SignUpServlet.REQUEST_URI
            );

            FakeResponseImpl response = new FakeResponseImpl();

            fakeAccountService.deleteSession(sId);

            signInServlet.doPut(request, response);

            assertTrue(fakeAccountService.checkSessionExists(sId));
            UserProfile userProfileLoggedIn = fakeAccountService.getSession(sId);
            assertTrue(userProfileGenerated.semanticEqual(userProfileLoggedIn));
            assertTrue(response.getStatusCode() == HttpServletResponse.SC_OK);
            assertTrue(response.getContentType().equals("application/json"));

            String responseContentStr = response.getContent();
            JSONObject responseContentJSON = new JSONObject(responseContentStr);
            Number id = (Number) responseContentJSON.get("id");
            assertTrue(id.longValue() == userProfileLoggedIn.getId());
        }
    }
}