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

    private static AtomicLong idGenerator = new AtomicLong(0);
    private AccountService accountService;

    public SignUpServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String email = request.getParameter("email");

        Boolean isGoodData = InputDataChecker.checkSignUp(login, password, email);

        Map<String, Object> pageVariables = new HashMap<>();
        int statusCode = 0;

        if( isGoodData && !accountService.checkUserExistsByLogin(login) ) {
            Long userId = idGenerator.getAndIncrement();
            UserProfile userProfile = new UserProfile(userId, login, password, email);
            statusCode = HttpServletResponse.SC_OK;
            accountService.addUser(userProfile);
            pageVariables.put("userId", userProfile.getId());
        }
        else {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
        }

        response.setStatus(statusCode);
        pageVariables.put("statusCode", statusCode);
        response.setContentType("application/json");
        response.getWriter().println(PageGenerator.getPage("SignUpResponse", pageVariables));
    }

}
