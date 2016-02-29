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

    private AccountService accountService;

    public SignInServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String sessionId = request.getSession().getId();

        Boolean isGoodData = InputDataChecker.checkSignIn(login, password, sessionId);

        Map<String, Object> pageVariables = new HashMap<>();
        int statusCode = 0;

        if( isGoodData && accountService.checkUserExistsByLogin(login)) {

            UserProfile userProfile = accountService.getUserByLogin(login);

            if(password.equals(userProfile.getPassword())) {
                statusCode = HttpServletResponse.SC_OK;
                if(accountService.checkSessionExists(sessionId))
                    accountService.deleteSession(sessionId);
                accountService.addSessions(sessionId, userProfile);
                pageVariables.put("userId", userProfile.getId());
            }
            else {
                statusCode = HttpServletResponse.SC_BAD_REQUEST;
            }

        }
        else {
            statusCode = HttpServletResponse.SC_BAD_REQUEST;
        }

        response.setStatus(statusCode);
        pageVariables.put("statusCode", statusCode);
        response.setContentType("application/json");
        response.getWriter().println(PageGenerator.getPage("SignInResponse", pageVariables));
    }
}
