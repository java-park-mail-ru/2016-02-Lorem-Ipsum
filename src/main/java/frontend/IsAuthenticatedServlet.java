package frontend;

import main.AccountService;
import main.UserProfile;
import datacheck.ElementaryChecker;
import datacheck.InputDataChecker;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IsAuthenticatedServlet extends HttpServlet {

    private AccountService accountService;

    public IsAuthenticatedServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        Map<String, Object> pageVariables = new HashMap<>();
        int statusCode = 0;

        if( ElementaryChecker.checkSessionId(sessionId) && accountService.checkSessionExists(sessionId) ) {
            UserProfile userProfile = accountService.getSessions(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            pageVariables.put("userId", userProfile.getId());
        }
        else {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        }

        response.setStatus(statusCode);
        pageVariables.put("statusCode", statusCode);
        response.setContentType("application/json");
        response.getWriter().println(PageGenerator.getPage("IsAuthenticatedResponse", pageVariables));
    }
}
