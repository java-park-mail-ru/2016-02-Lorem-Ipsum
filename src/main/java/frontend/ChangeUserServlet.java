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

public class ChangeUserServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user/";

    public static final Logger LOGGER = LogManager.getLogger(ChangeUserServlet.class);

    public ChangeUserServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonBody = RequestParser.parseRequestBody(request.getReader());
        String login = jsonBody.getString("login");
        String password = jsonBody.getString("password");
        String email = jsonBody.getString("email");
        String sessionId = request.getSession().getId();

        LOGGER.debug("ChangeUser request got with params: login:\"{}\", password:\"{}\", email:\"{}\", session:\"{}\"",
                login, password, email, sessionId);

        Boolean isGoodData = InputDataChecker.checkChangeUser(login, password, email)
                && ElementaryChecker.checkSessionId(sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {
            if(!isGoodData)
                throw new Exception("Bad data.");
            if(!accountService.checkSessionExists(sessionId))
                throw new Exception("User is not logged in.");

            UserProfile userProfile = accountService.getSession(sessionId);

            if(accountService.checkUserExistsByLogin(login) && !userProfile.getLogin().equals(login))
                throw new Exception("Assumption to change another user");

            LOGGER.debug("User before change: {}", userProfile.toJSON());
            statusCode = HttpServletResponse.SC_OK;
            userProfile.setLogin(login);
            userProfile.setPassword(password);
            userProfile.setEmail(email);
            accountService.changeUser(userProfile);
            dataToSend.put("id", userProfile.getUserId());
            LOGGER.debug("Success. Changed user: {}", userProfile.toJSON());
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            dataToSend.put("status", statusCode);
            dataToSend.put("message", "Чужой юзер.");
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
