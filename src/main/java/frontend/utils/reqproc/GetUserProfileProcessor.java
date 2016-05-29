package frontend.utils.reqproc;

import database.exceptions.sesexceptions.GetSessionException;
import database.exceptions.sesexceptions.SessionExistsException;
import database.exceptions.userexceptions.GetUserException;
import database.exceptions.userexceptions.UserExistsException;
import datacheck.ElementaryChecker;
import datacheck.ElementaryGetter;
import frontend.utils.exceptions.ForbiddenException;
import main.IAccountService;
import main.UserProfile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Installed on 29.05.2016.
 */
public class GetUserProfileProcessor extends RequestProcessor {

    private final String sessionId;
    private final Long userId;

    public GetUserProfileProcessor(
            HttpServletRequest request,
            HttpServletResponse response,
            IAccountService accountService
    ) {
        super(request, response, accountService);
        sessionId = request.getSession().getId();
        String sId = request.getRequestURI();
        sId = sId.substring(sId.lastIndexOf('/') + 1, sId.length());
        userId = ElementaryGetter.getLongOrNull(sId);
    }


    @Override
    protected void verify()
        throws ForbiddenException,
            UserExistsException,
            SessionExistsException
    {
        if(!ElementaryChecker.checkUserId(userId))
            throw new ForbiddenException("Bad data");
        if(!accountService.checkUserExistsById(userId))
            throw new ForbiddenException("Such user does not exist.");
        if(!accountService.checkSessionExists(sessionId))
            throw new ForbiddenException("Request from unauthorized user.");
    }

    @Override
    protected void execInternal()
        throws ForbiddenException,
            GetUserException,
            GetSessionException
    {
        UserProfile userProfile = accountService.getUserById(userId);
        UserProfile sessionProfile = accountService.getSession(sessionId);
        Long idProfile = userProfile.getUserId();
        Long idSessionProfile = sessionProfile.getUserId();

        if(!idProfile.equals(idSessionProfile))
            throw new ForbiddenException("Assumption to get information about another user profile.");

        statusCode = HttpServletResponse.SC_OK;
        dataToSend.put("id", userProfile.getUserId());
        dataToSend.put("login", userProfile.getLogin());
        dataToSend.put("email", userProfile.getEmail());
        LOGGER.debug("Success. SessionId: {}. User requested: {}. User changed: {}",
                sessionProfile.toJSON(), userProfile.toJSON());
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
        catch (UserExistsException e) {
            LOGGER.debug("Unable to check if user exists. Reason: {}", e.getMessage());
        }
        catch (GetUserException e) {
            LOGGER.debug("Unable to get user. Reason: {}", e.getMessage());
        }
    }

}
