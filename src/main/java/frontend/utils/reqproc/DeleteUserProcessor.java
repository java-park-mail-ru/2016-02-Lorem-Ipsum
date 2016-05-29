package frontend.utils.reqproc;

import database.exceptions.sesexceptions.GetSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.DeleteUserException;
import datacheck.ElementaryChecker;
import frontend.utils.exceptions.ForbiddenException;
import main.IAccountService;
import main.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Installed on 29.05.2016.
 */
public class DeleteUserProcessor extends RequestProcessor {

    private final String sessionId;

    public DeleteUserProcessor(
            HttpServletRequest request,
            HttpServletResponse response,
            IAccountService accountService
    ) {
        super(request, response, accountService);
        sessionId = request.getSession().getId();
    }

    @Override
    protected void verify()
        throws ForbiddenException,
            SessionExistsException
    {
        if(!ElementaryChecker.checkSessionId(sessionId))
            throw new ForbiddenException("Bad data");
        if(!accountService.checkSessionExists(sessionId))
            throw new ForbiddenException("Request from unauthorized user.");
    }

    @Override
    protected void execInternal()
        throws GetSessionException,
            DeleteUserException
    {
        UserProfile userProfile = accountService.getSession(sessionId);
        statusCode = HttpServletResponse.SC_OK;
        accountService.deleteUser(sessionId, userProfile);
    }

    @Override
    public void execute(int failureCode) {
        try {
            super.execute(failureCode);
        }
        catch (SessionExistsException e) {
            LOGGER.debug("Unable to check if session exists. Reason: {}", e.getMessage());
        }
        catch (GetSessionException e) {
            LOGGER.debug("Unable to get session. Reason: {}", e.getMessage());
        }
        catch (DeleteUserException e) {
            LOGGER.debug("Unable to delete user. Reason: {}", e.getMessage());
        }
    }

}
