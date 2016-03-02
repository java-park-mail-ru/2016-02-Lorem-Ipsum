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

    private final AccountService accountService;

    public ChangeUserServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String sessionId = request.getSession().getId();

        Boolean isGoodData = InputDataChecker.checkChangeUser(login, password, email);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( isGoodData && accountService.checkSessionExists(sessionId)) {
            UserProfile userProfile = accountService.getSessions(sessionId);
            if(!accountService.checkUserExistsByLogin(login) || userProfile.getLogin().equals(login)) {
                statusCode = HttpServletResponse.SC_OK;
                userProfile.setLogin(login);
                userProfile.setPassword(password);
                userProfile.setEmail(email);
                dataToSend.put("id", userProfile.getId());
            }
            else {
                statusCode = HttpServletResponse.SC_FORBIDDEN;
            }
        }
        else {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        }

        if(statusCode == HttpServletResponse.SC_FORBIDDEN) {
            dataToSend.put("status", statusCode);
            dataToSend.put("message", "Чужой юзер.");
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        response.getWriter().println(PageGenerator.getPage("Response", pageVariables));
    }

}
