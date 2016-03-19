package frontend;

import datacheck.*;
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

public class GetUserProfileServlet extends HttpServlet {

    private final IAccountService accountService;
    public static final String REQUEST_URI = "/user/*";

    public GetUserProfileServlet(IAccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        Long userId = ElementaryGetter.getLongOrNull(sId);

        String sessionId = request.getSession().getId();

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        if( ElementaryChecker.checkUserId(userId) && accountService.checkUserExistsById(userId)
                && accountService.checkSessionExists(sessionId)) {
            UserProfile userProfile = accountService.getUserById(userId);
            UserProfile sessionProfile = accountService.getSession(sessionId);
            Long idProfile = userProfile.getId();
            Long idSessionProfile = sessionProfile.getId();
            if(idProfile.equals(idSessionProfile)) {
                statusCode = HttpServletResponse.SC_OK;
                dataToSend.put("id", userProfile.getId());
                dataToSend.put("login", userProfile.getLogin());
                dataToSend.put("email", userProfile.getEmail());
            }
            else {
                statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            }
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
