package frontend;

import main.AccountService;
import main.UserProfile;
import datacheck.InputDataChecker;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SignInServlet extends HttpServlet {

    private final AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String sessionId = request.getSession().getId();

        Boolean isGoodData = InputDataChecker.checkSignIn(login, password, sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( isGoodData && accountService.checkUserExistsByLogin(login)) {

            UserProfile userProfile = accountService.getUserByLogin(login);

            if(password.equals(userProfile.getPassword())) {
                statusCode = HttpServletResponse.SC_OK;
                if(accountService.checkSessionExists(sessionId))
                    accountService.deleteSession(sessionId);
                accountService.addSessions(sessionId, userProfile);
                dataToSend.put("id", userProfile.getId());
            }
            else {
                statusCode = HttpServletResponse.SC_BAD_REQUEST;
            }

        }
        else {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        response.getWriter().println(PageGenerator.getPage("Response", pageVariables));
    }
}
