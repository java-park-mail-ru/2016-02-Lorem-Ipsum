package frontend;

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

public class SignInServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/session";

    public static final Logger LOGGER = LogManager.getLogger(SignInServlet.class);

    public SignInServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {
        JSONObject jsonBody = RequestParser.parseRequestBody(request.getReader());
        String login = jsonBody.getString("login");
        String password = jsonBody.getString("password");
        String sessionId = request.getSession().getId();

        LOGGER.debug("SignInServlet request got with params: login:\"{}\", password:\"{}\", session:\"{}\"",
                login, password, sessionId);

        Boolean isGoodData = InputDataChecker.checkSignIn(login, password, sessionId);

        Map<String, Object> dataToSend = new HashMap<>();

        int statusCode;

        try {
            if(!isGoodData)
                throw new Exception("Bad data.");
            if(!accountService.checkUserExistsByLogin(login))
                throw new Exception("User with such login does not exist.");

            UserProfile userProfile = accountService.getUserByLogin(login);

            if(!password.equals(userProfile.getPassword()))
                throw new Exception("Incorrect password.");

            statusCode = HttpServletResponse.SC_OK;
            if(accountService.checkSessionExists(sessionId)) {
                accountService.deleteSession(sessionId);
                LOGGER.debug("[To improve?] Session existed. Just relogin.");
            }
            accountService.addSession(sessionId, userProfile);
            dataToSend.put("id", userProfile.getUserId());
            LOGGER.debug("Success. Logged in user: {}", userProfile.toJSON());
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

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        Map<String, Object> pageVariables = new HashMap<>();
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String sessionId = request.getSession().getId();

        Boolean isGoodData = InputDataChecker.checkSignIn(login, password, sessionId);

        //noinspection OverlyBroadCatchBlock
        try {
            if(!isGoodData)
                throw new Exception("Bad data.");
            if(!accountService.checkUserExistsByLogin(login))
                throw new Exception("User with such login does not exist.");

            UserProfile userProfile = accountService.getUserByLogin(login);

            if(!password.equals(userProfile.getPassword()))
                throw new Exception("Incorrect password.");

            if(accountService.checkSessionExists(sessionId)) {
                accountService.deleteSession(sessionId);
                LOGGER.debug("Session existed. Just relogin.");
            }

            response.setStatus(HttpServletResponse.SC_OK);
            accountService.addSession(sessionId, userProfile);
            pageVariables.put("myName", userProfile.getLogin());
            response.getWriter().println(PageGenerator.getPage("game.html", pageVariables));
            LOGGER.debug("Success. Logged in user: {}", userProfile.toJSON());
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().println(PageGenerator.getPage("index.html", pageVariables));
            LOGGER.debug("Denied. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        response.setContentType("text/html;charset=utf-8");
    }

}
