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

public class ChangeUserServlet extends HttpServlet {

    private AccountService accountService;

    public ChangeUserServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String sessionId = request.getSession().getId();

        Boolean isGoodData = InputDataChecker.checkChangeUser(login, password, email);

        Map<String, Object> pageVariables = new HashMap<>();
        int statusCode = 0;

        if( isGoodData && accountService.checkSessionExists(sessionId)
                && !accountService.checkUserExistsByLogin(login)) {
            UserProfile userProfile = accountService.getSessions(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            userProfile.setLogin(login);
            userProfile.setPassword(password);
            userProfile.setEmail(email);
            pageVariables.put("userId", userProfile.getId());
        }
        else {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        }

        response.setStatus(statusCode);
        pageVariables.put("statusCode", statusCode);
        response.setContentType("application/json");
        response.getWriter().println(PageGenerator.getPage("ChangeUserResponse", pageVariables));
    }

}
