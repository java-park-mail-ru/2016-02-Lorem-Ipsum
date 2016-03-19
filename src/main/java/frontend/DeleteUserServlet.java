package frontend;

import datacheck.ElementaryChecker;
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

public class DeleteUserServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user/";

    public static final Logger LOGGER = LogManager.getLogger(DeleteUserServlet.class);

    public DeleteUserServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        LOGGER.debug("DeleteUser request got with params: session:\"{}\"", sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {
            if(!ElementaryChecker.checkSessionId(sessionId))
                throw new Exception("Bad data");
            if(!accountService.checkSessionExists(sessionId))
                throw new Exception("Request from unauthorized user.");

            UserProfile userProfile = accountService.getSession(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            accountService.deleteUser(sessionId, userProfile);

            LOGGER.debug("Success. SessionId: {}. User deleted: {}",
                    sessionId, userProfile.toJSON());
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
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
