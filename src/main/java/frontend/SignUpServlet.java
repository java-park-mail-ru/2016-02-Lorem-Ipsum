package frontend;

import datacheck.ElementaryChecker;
import frontend.utils.RequestParser;
import main.IAccountService;
import main.UserProfile;
import datacheck.InputDataChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class SignUpServlet extends HttpServlet {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user";

    public static final Logger LOGGER = LogManager.getLogger(SignUpServlet.class);

    public SignUpServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonBody = RequestParser.parseRequestBody(request.getReader());
        String login = jsonBody.getString("login");
        String password = jsonBody.getString("password");
        String email = jsonBody.getString("email");
        String sessionId = request.getSession().getId();

        LOGGER.debug("SignUpServlet request got with params: login:\"{}\", password:\"{}\", email:\"{}\", session:\"{}\"",
                login, password, email, sessionId);

        Boolean isGoodData = InputDataChecker.checkSignUp(login, password, email) &&
                ElementaryChecker.checkSessionId(sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {

            if(!isGoodData)
                throw new Exception("Bad data.");
            if(accountService.checkUserExistsByLogin(login))
                throw new Exception("User with such login already exists.");
            if(accountService.checkSessionExists(sessionId))
                throw new Exception("User is logged in.");

            //Long userId = ID_GENERATOR.getAndIncrement();
            UserProfile userProfile = new UserProfile(-1, login, password, email);
            statusCode = HttpServletResponse.SC_OK;
            Long userId = accountService.addUser(userProfile);
            dataToSend.put("id", userId);
            LOGGER.debug("Success. New user: {}", userProfile.toJSON());
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            LOGGER.debug("Denied. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        String responseContent = PageGenerator.getPage("Response", pageVariables);
        response.getWriter().println(responseContent);
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));
    }

}
