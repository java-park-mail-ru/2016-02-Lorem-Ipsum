package frontend.utils.reqproc;

import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.ChangeUserException;
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
public class ChangeUserProcessor extends RequestProcessor {

    private JSONObject jsonBody;
    private String login;
    private String password;
    private String email;
    private String sessionId;

    public ChangeUserProcessor(HttpServletRequest request, HttpServletResponse response, IAccountService accountService)
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
        throws ForbiddenException,
            SessionExistsException
    {
        Boolean isGoodData = InputDataChecker.checkChangeUser(login, password, email)
                && ElementaryChecker.checkSessionId(sessionId);

        if(!isGoodData)
            throw new ForbiddenException("Bad data.");
        if(!accountService.checkSessionExists(sessionId))
            throw new ForbiddenException("User is not logged in.");
    }

    @Override
    protected void execInternal()
        throws ForbiddenException,
            UserExistsException,
            ChangeUserException
    {
        UserProfile userProfile = accountService.getSession(sessionId);

        if(accountService.checkUserExistsByLogin(login) && !userProfile.getLogin().equals(login))
            throw new ForbiddenException("Assumption to change another user");

        LOGGER.debug("User before change: {}", userProfile.toJSON());
        statusCode = HttpServletResponse.SC_OK;
        userProfile.setLogin(login);
        userProfile.setPassword(password);
        userProfile.setEmail(email);
        accountService.changeUser(userProfile);
        dataToSend.put("id", userProfile.getUserId());
        LOGGER.debug("Success. Changed user: {}", userProfile.toJSON());
    }

    @Override
    public void execute(int failureCode) {
        try {
            super.execute(failureCode);
        }
        catch (SessionExistsException e) {
            LOGGER.debug("Unable to check if session exists. Reason: {}", e.getMessage());
        }
        catch (UserExistsException e) {
            LOGGER.debug("Unable to check if user exists. Reason: {}", e.getMessage());
        }
        catch (ChangeUserException e) {
            LOGGER.debug("Unable to change user. Reason: {}", e.getMessage());
        }
    }

}
