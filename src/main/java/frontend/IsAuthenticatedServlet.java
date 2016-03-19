package frontend;

import main.AccountService;
import main.IAccountService;
import main.UserProfile;
import datacheck.ElementaryChecker;
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

    public IsAuthenticatedServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( ElementaryChecker.checkSessionId(sessionId) && accountService.checkSessionExists(sessionId) ) {
            UserProfile userProfile = accountService.getSession(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            dataToSend.put("id", userProfile.getId());
        }
        else {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        response.getWriter().println(PageGenerator.getPage("Response", pageVariables));
    }
}
