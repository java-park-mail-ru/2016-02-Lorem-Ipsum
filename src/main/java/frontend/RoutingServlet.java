package frontend;

import database.DbService;
import frontend.utils.reqproc.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoutingServlet extends HttpServlet {

    private final DbService accountService;

    public RoutingServlet(DbService accountService) {
        this.accountService = accountService;
    }

    private void changeUser(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {
        ChangeUserProcessor changeUserProcessor = new ChangeUserProcessor(request, response, accountService);
        changeUserProcessor.execute(HttpServletResponse.SC_FORBIDDEN);
    }

    private void deleteUser(HttpServletRequest request,
                            HttpServletResponse response) {
        DeleteUserProcessor deleteUserProcessor = new DeleteUserProcessor(request, response, accountService);
        deleteUserProcessor.execute(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void getBestResults(HttpServletRequest request,
                                HttpServletResponse response) {
        GetBestResultsProcessor getBestResultsProcessor = new GetBestResultsProcessor(request, response,
                accountService, accountService);
        getBestResultsProcessor.execute(HttpServletResponse.SC_SERVICE_UNAVAILABLE);
    }

    private void getUserProfile(HttpServletRequest request,
                                HttpServletResponse response){
        GetUserProfileProcessor getUserProfileProcessor = new GetUserProfileProcessor(request, response, accountService);
        getUserProfileProcessor.execute(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void isAuthenticated(HttpServletRequest request,
                                 HttpServletResponse response) {
        IsAuthenticatedProcessor isAuthenticatedProcessor = new IsAuthenticatedProcessor(request, response, accountService);
        isAuthenticatedProcessor.execute(HttpServletResponse.SC_UNAUTHORIZED);
    }

    private void logOut(HttpServletRequest request,
                        HttpServletResponse response) {
        LogOutProcessor logOutProcessor = new LogOutProcessor(request, response, accountService);
        logOutProcessor.execute(HttpServletResponse.SC_FORBIDDEN);
    }

    private void signIn(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        SignInProcessor signInProcessor = new SignInProcessor(request, response, accountService);
        signInProcessor.execute(HttpServletResponse.SC_FORBIDDEN);
    }

    private void signUp(HttpServletRequest request,
                        HttpServletResponse response) throws IOException {
        SignUpProcessor signUpProcessor = new SignUpProcessor(request, response, accountService);
        signUpProcessor.execute(HttpServletResponse.SC_FORBIDDEN);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        switch (sId) {
            case "session":
                isAuthenticated(request, response);
                break;
            case "score":
                getBestResults(request, response);
                break;
            default:
                getUserProfile(request, response);
                break;
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        changeUser(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("user")) {
            signUp(request, response);
        } else if(sId.equals("session")) {
            signIn(request, response);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("session")) {
            logOut(request, response);
        } else {
            deleteUser(request, response);
        }
    }

}
