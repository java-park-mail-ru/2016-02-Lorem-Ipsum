package frontend.utils.reqproc;

import frontend.utils.exceptions.ForbiddenException;
import main.IAccountService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import templater.PageGenerator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Installed on 29.05.2016.
 */
public abstract class RequestProcessor {

    public static final Logger LOGGER = LogManager.getLogger(RequestProcessor.class);
    protected final HttpServletRequest request;
    protected final HttpServletResponse response;
    protected final Map<String, Object> dataToSend = new HashMap<>();
    protected int statusCode;
    protected final IAccountService accountService;

    protected abstract void verify() throws ForbiddenException;
    protected abstract void execInternal() throws ForbiddenException;

    public RequestProcessor(HttpServletRequest request, HttpServletResponse response, IAccountService accountService) {
        this.request = request;
        this.response = response;
        this.accountService = accountService;
    }

    public void execute(int failureCode) {
        try {
            try {
                verify();
                execInternal();
                sendResponse();
            } catch (ForbiddenException e) {
                LOGGER.debug("Action forbidden. Reason: {}", e.getMessage());
                dataToSend.put("error", e.getMessage());
                statusCode = failureCode;
                sendResponse();
            }
        }
        catch (IOException e) {
            LOGGER.debug("Cannot write response. Reason: {}", e.getMessage());
        }
    }

    public void sendResponse() throws IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        String responseContent = PageGenerator.getPage("Response", pageVariables);
        response.getWriter().println(responseContent);
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));
    }

}
