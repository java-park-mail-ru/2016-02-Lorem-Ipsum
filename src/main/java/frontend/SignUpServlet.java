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
import java.util.concurrent.atomic.AtomicLong;

public class SignUpServlet extends HttpServlet {

    private static final AtomicLong ID_GENERATOR = new AtomicLong(0);
    private final AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Boolean isGoodData = InputDataChecker.checkSignUp(login, password, email);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( isGoodData && !accountService.checkUserExistsByLogin(login) ) {
            Long userId = ID_GENERATOR.getAndIncrement();
            UserProfile userProfile = new UserProfile(userId, login, password, email);
            statusCode = HttpServletResponse.SC_OK;
            accountService.addUser(userProfile);
            dataToSend.put("id", userProfile.getId());
        }
        else {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        response.getWriter().println(PageGenerator.getPage("Response", pageVariables));
    }

}
