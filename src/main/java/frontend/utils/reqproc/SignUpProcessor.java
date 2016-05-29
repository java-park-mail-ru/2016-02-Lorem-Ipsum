package frontend.utils.reqproc;

import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.UserExistsException;
import datacheck.ElementaryChecker;
import datacheck.InputDataChecker;
import frontend.utils.RequestBodyParser;
import frontend.utils.exceptions.ForbiddenException;
import main.IAccountService;
import main.UserProfile;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Installed on 29.05.2016.
 */
public class SignUpProcessor extends RequestProcessor {

    private JSONObject jsonBody;
    private String login;
    private String password;
    private String email;
    private String sessionId;

    public SignUpProcessor(HttpServletRequest request, HttpServletResponse response, IAccountService accountService)
    throws IOException
    {
        super(request, response, accountService);
        jsonBody = RequestBodyParser.parseRequestBody(request.getReader());
        login = jsonBody.getString("login");
        password = jsonBody.getString("password");
        email = jsonBody.getString("email");
        sessionId = request.getSession().getId();
    }

    @Override
    protected void verify()
            throws
            ForbiddenException,
            UserExistsException,
            SessionExistsException
    {
        Boolean isGoodData = InputDataChecker.checkSignUp(login, password, email) &&
                ElementaryChecker.checkSessionId(sessionId);

        if(!isGoodData)
            throw new ForbiddenException("Bad data.");
        if(accountService.checkUserExistsByLogin(login))
            throw new ForbiddenException("User with such login already exists.");
        if(accountService.checkSessionExists(sessionId))
            throw new ForbiddenException("User is logged in.");
    }

    @Override
    protected void execInternal() {
        UserProfile userProfile = new UserProfile(-1, login, password, email);
        statusCode = HttpServletResponse.SC_OK;
        Long userId = accountService.addUser(userProfile);
        dataToSend.put("id", userId);
    }

    @Override
    public void execute(int failureCode)
    {
        try {
            super.execute(failureCode);
        }
        catch (SessionExistsException e) {
            LOGGER.debug("Unable to check if session exists. Reason: {}", e.getMessage());
        }
        catch (UserExistsException e) {
            LOGGER.debug("Unable to check if user exists. Reason: {}", e.getMessage());
        }
    }

}
