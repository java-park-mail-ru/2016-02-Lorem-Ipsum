package frontend;

import datacheck.*;
import main.AccountService;
import main.IAccountService;
import main.UserProfile;
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

public class GetUserProfileServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user/";

    public static final Logger LOGGER = LogManager.getLogger(GetUserProfileServlet.class);

    public GetUserProfileServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        Long userId = ElementaryGetter.getLongOrNull(sId);

        String sessionId = request.getSession().getId();

        LOGGER.debug("GetUserProfile request got with params: session:\"{}\", if of user to change: \"{}\" ",
                sessionId, userId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {
            if(!ElementaryChecker.checkUserId(userId))
                throw new Exception("Bad data");
            if(!accountService.checkUserExistsById(userId))
                throw new Exception("Such user does not exist.");
            if(!accountService.checkSessionExists(sessionId))
                throw new Exception("Request from unauthorized user.");

            UserProfile userProfile = accountService.getUserById(userId);
            UserProfile sessionProfile = accountService.getSession(sessionId);
            Long idProfile = userProfile.getId();
            Long idSessionProfile = sessionProfile.getId();

            if(!idProfile.equals(idSessionProfile))
                throw new Exception("Assumption to get information about another user profile.");

            statusCode = HttpServletResponse.SC_OK;
            dataToSend.put("id", userProfile.getId());
            dataToSend.put("login", userProfile.getLogin());
            dataToSend.put("email", userProfile.getEmail());
            LOGGER.debug("Success. SessionId: {}. User requested: {}. User changed: {}",
                    sessionProfile.toJSON(), userProfile.toJSON());
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
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));
    }
}
