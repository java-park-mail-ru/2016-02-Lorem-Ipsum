package frontend.utils.reqproc;

import datacheck.ElementaryChecker;
import frontend.utils.exceptions.ForbiddenException;
import main.IAccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Installed on 29.05.2016.
 */
public class LogOutProcessor extends RequestProcessor {

    private final String sessionId;

    public LogOutProcessor(HttpServletRequest request, HttpServletResponse response, IAccountService accountService) {
        super(request, response, accountService);
        sessionId = request.getSession().getId();
    }

    @Override
    protected void verify()
        throws  ForbiddenException
    {
        if(!ElementaryChecker.checkSessionId(sessionId))
            throw new ForbiddenException("Bad data.");
        if(!accountService.checkSessionExists(sessionId))
            throw new ForbiddenException("Not logged in.");
    }

    @Override
    protected void execInternal() {
        accountService.deleteSession(sessionId);
        LOGGER.debug("Success. SessionId: {}", sessionId);
        statusCode = HttpServletResponse.SC_OK;
    }

}
