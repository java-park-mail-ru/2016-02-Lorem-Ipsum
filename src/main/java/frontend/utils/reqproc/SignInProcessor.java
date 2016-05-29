package frontend.utils.reqproc;

import database.exceptions.sesexceptions.AddSessionException;
import database.exceptions.sesexceptions.DeleteSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.GetUserException;
import database.exceptions.userexceptions.UserExistsException;
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
public class SignInProcessor extends RequestProcessor {

    private JSONObject jsonBody;
    private String login;
    private String password;
    private String email;
    private String sessionId;

    public SignInProcessor(HttpServletRequest request, HttpServletResponse response, IAccountService accountService)
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
            UserExistsException
    {
        Boolean isGoodData = InputDataChecker.checkSignIn(login, password, sessionId);

        if(!isGoodData)
            throw new ForbiddenException("Bad data.");
        if(!accountService.checkUserExistsByLogin(login))
            throw new ForbiddenException("User with such login does not exist.");
    }

    @Override
    protected void execInternal()
        throws ForbiddenException,
            GetUserException,
            SessionExistsException,
            DeleteSessionException,
            AddSessionException
    {
        UserProfile userProfile = accountService.getUserByLogin(login);

        if(!password.equals(userProfile.getPassword()))
            throw new ForbiddenException("Incorrect password.");

        statusCode = HttpServletResponse.SC_OK;
        if(accountService.checkSessionExists(sessionId)) {
            accountService.deleteSession(sessionId);
            LOGGER.debug("[To improve?] Session existed. Just relogin.");
        }
        accountService.addSession(sessionId, userProfile);
        dataToSend.put("id", userProfile.getUserId());
        LOGGER.debug("Success. Logged in user: {}", userProfile.toJSON());

    }

    @Override
    public void execute(int failureCode) {
        try {
            super.execute(failureCode);
        }
        catch (GetUserException e) {
            LOGGER.debug("Unable to get user. Reason: {}", e.getMessage());
        }
        catch (UserExistsException e) {
            LOGGER.debug("Unable to check if user exists. Reason: {}", e.getMessage());
        }
        catch (SessionExistsException e) {
            LOGGER.debug("Unable to check if session exists. Reason: {}", e.getMessage());
        }
        catch (AddSessionException e) {
            LOGGER.debug("Unable to add session. Reason: {}", e.getMessage());
        }
        catch (DeleteSessionException e) {
            LOGGER.debug("Unable to delete session. Reason: {}", e.getMessage());
        }
    }

}
