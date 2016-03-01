package frontend;

import main.AccountService;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoutingServlet extends HttpServlet {

    private SignInServlet signInServlet;
    private SignUpServlet signUpServlet;
    private IsAuthenticatedServlet isAuthenticatedServlet;
    private GetUserProfileServlet getUserProgileServlet;
    private ChangeUserServlet changeUserServlet;
    private DeleteUserServlet deleteUserServlet;

    public RoutingServlet(AccountService accountService) {
        signInServlet = new SignInServlet(accountService);
        signUpServlet = new SignUpServlet(accountService);
        isAuthenticatedServlet = new IsAuthenticatedServlet(accountService);
        getUserProgileServlet = new GetUserProfileServlet(accountService);
        changeUserServlet = new ChangeUserServlet(accountService);
        deleteUserServlet = new DeleteUserServlet(accountService);
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("session")) {
            isAuthenticatedServlet.doGet(request, response);
        } else {
            getUserProgileServlet.doGet(request, response);
        }
    }

    @Override
    public void doPost(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        if(sId.equals("user")) {
            signUpServlet.doPost(request, response);
        } else if(sId.equals("session")) {
            signInServlet.doPost(request, response);
        }
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        changeUserServlet.doPut(request, response);
    }

    @Override
    public void doDelete(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {
        deleteUserServlet.doDelete(request, response);
    }

}
