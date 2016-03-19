package frontend;

import main.AccountService;
import main.IAccountService;
import main.UserProfile;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class DeleteUserServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user/*";

    public DeleteUserServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( accountService.checkSessionExists(sessionId)) {
            UserProfile userProfile = accountService.getSession(sessionId);
            statusCode = HttpServletResponse.SC_OK;
            accountService.deleteUser(sessionId, userProfile);
        }
        else {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
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
