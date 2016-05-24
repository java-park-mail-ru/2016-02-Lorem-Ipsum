package frontend;

import main.IAccountService;
import main.UserProfile;
import datacheck.ElementaryChecker;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IsAuthenticatedServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/session";

    public static final Logger LOGGER = LogManager.getLogger(IsAuthenticatedServlet.class);

    public IsAuthenticatedServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        LOGGER.debug("IsAuthenticated request got with params: session:\"{}\"", sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {
            if(!ElementaryChecker.checkSessionId(sessionId))
                throw new Exception("Bad data.");
            if(!accountService.checkSessionExists(sessionId))
                throw new Exception("Not logged in.");

            UserProfile userProfile = accountService.getSession(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            dataToSend.put("id", userProfile.getUserId());
            LOGGER.debug("Success. SessionId: {}. User: {}", userProfile.toJSON());
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            LOGGER.debug("Denied. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        String responseContent = PageGenerator.getPage("Response", pageVariables);
        response.getWriter().println(responseContent);
        LOGGER.debug(   "Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));
    }
}
