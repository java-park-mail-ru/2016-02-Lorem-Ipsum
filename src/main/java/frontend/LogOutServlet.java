package frontend;

import main.AccountService;
import datacheck.ElementaryChecker;
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

public class LogOutServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/session";

    public static final Logger LOGGER = LogManager.getLogger(LogOutServlet.class);

    public LogOutServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();

        Map<String, Object> dataToSend = new HashMap<>();

        LOGGER.debug("LogOut request got with params: session:\"{}\"", sessionId);;

        try {
            if(!ElementaryChecker.checkSessionId(sessionId))
                throw new Exception("Bad data.");
            if(!accountService.checkSessionExists(sessionId))
                throw new Exception("Not logged in.");

            accountService.deleteSession(sessionId);
            LOGGER.debug("Success. SessionId: {}", sessionId);
        }
        catch (Exception e) {
            LOGGER.debug("Denied. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        int statusCode = HttpServletResponse.SC_OK;
        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        String responseContent = PageGenerator.getPage("Response", pageVariables);
        response.getWriter().println(responseContent);
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));
    }
}
