package frontend;

import database.DbService;
import main.IAccountService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoutingServlet extends HttpServlet {

    private final SignInServlet signInServlet;
    private final SignUpServlet signUpServlet;
    private final IsAuthenticatedServlet isAuthenticatedServlet;
    private final GetUserProfileServlet getUserProgileServlet;
    private final ChangeUserServlet changeUserServlet;
    private final DeleteUserServlet deleteUserServlet;
    private final LogOutServlet logOutServlet;
    private final SaveGameScoreServlet saveGameScoreServlet;
    private final GetBestResultsServlet getBestResultsServlet;

    public RoutingServlet(DbService accountService) {
        signInServlet = new SignInServlet(accountService);
        signUpServlet = new SignUpServlet(accountService);
        isAuthenticatedServlet = new IsAuthenticatedServlet(accountService);
        getUserProgileServlet = new GetUserProfileServlet(accountService);
        changeUserServlet = new ChangeUserServlet(accountService);
        deleteUserServlet = new DeleteUserServlet(accountService);
        logOutServlet = new LogOutServlet(accountService);
        saveGameScoreServlet = new SaveGameScoreServlet(accountService);
        getBestResultsServlet = new GetBestResultsServlet(accountService);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("session")) {
            isAuthenticatedServlet.doGet(request, response);
        }
        else if(sId.equals("score")) {
            getBestResultsServlet.doGet(request, response);
        }
        else {
            getUserProgileServlet.doGet(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        changeUserServlet.doPost(request, response);
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("user")) {
            signUpServlet.doPut(request, response);
        } else if(sId.equals("session")) {
            signInServlet.doPut(request, response);
        } else if(sId.equals("score")) {
            saveGameScoreServlet.doPut(request, response);
        }
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("session")) {
            logOutServlet.doDelete(request, response);
        } else {
            deleteUserServlet.doDelete(request, response);
        }
    }

}
