package frontend;

import datacheck.*;
import main.AccountService;
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

    private AccountService accountService;

    public GetUserProfileServlet(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                       HttpServletResponse response) throws ServletException, IOException {

        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        Long userId = ElementaryGetter.getLongOrNull(sId);

        String sessionId = request.getSession().getId();

        Map<String, Object> pageVariables = new HashMap<>();
        int statusCode = 0;

        if( ElementaryChecker.checkUserId(userId) && accountService.checkUserExistsById(userId)
                && accountService.checkSessionExists(sessionId)) {
            UserProfile userProfile = accountService.getUserById(userId);
            UserProfile sessionProfile = accountService.getSessions(sessionId);
            Long idProfile = userProfile.getId();
            Long idSessionProfile = sessionProfile.getId();
            if(idProfile.equals(idSessionProfile)) {
                statusCode = HttpServletResponse.SC_OK;
                pageVariables.put("userId", userProfile.getId());
                pageVariables.put("userLogin", userProfile.getLogin());
                pageVariables.put("userEmail", userProfile.getEmail());
            }
            else {
                statusCode = HttpServletResponse.SC_UNAUTHORIZED;
            }
        }
        else {
            statusCode = HttpServletResponse.SC_UNAUTHORIZED;
        }

        response.setStatus(statusCode);
        pageVariables.put("statusCode", statusCode);
        response.setContentType("application/json");
        response.getWriter().println(PageGenerator.getPage("GetUserProfileResponse", pageVariables));
    }
}
