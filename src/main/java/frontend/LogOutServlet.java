package frontend;

import main.AccountService;
import datacheck.ElementaryChecker;
import main.IAccountService;
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

    public LogOutServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sessionId = request.getSession().getId();

        Map<String, Object> dataToSend = new HashMap<>();

        if( ElementaryChecker.checkSessionId(sessionId) && accountService.checkSessionExists(sessionId) ) {
            accountService.deleteSession(sessionId);
        }

        int statusCode = HttpServletResponse.SC_OK;
        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        response.getWriter().println(PageGenerator.getPage("Response", pageVariables));
    }
}
