package frontend;

import database.DbService;
import database.IDbService;
import frontend.utils.RequestParser;
import main.IAccountService;
import main.IGame;
import main.UserProfile;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Installed on 03.04.2016.
 */
public class SaveGameScoreServlet extends HttpServlet {

    private final IGame gameService;
    public static final String REQUEST_URI = "/score";

    public static final Logger LOGGER = LogManager.getLogger(SaveGameScoreServlet.class);

    public SaveGameScoreServlet(IGame accountService) {
        this.gameService = accountService;
    }

    @Override
    public void doPut(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        JSONObject jsonBody = RequestParser.parseRequestBody(request.getReader());
        long userId = jsonBody.getLong("id");
        long userScore = jsonBody.getLong("score");
        String sessionId = request.getSession().getId();

        LOGGER.debug("SaveGameResultServlet request got with param: id:\"{}\", score:\"{}\", session:\"{}\"",
                userId, userScore, sessionId);

        Map<String, Object> dataToSend = new HashMap<>();
        int statusCode;

        try {

            if(!gameService.checkUserExistsById(userId))
                throw new Exception("User with such id does not exist.");

            gameService.saveGameResultByUserId(userId, userScore);
            statusCode = HttpServletResponse.SC_OK;
            dataToSend.put("result", "OK");
            LOGGER.debug("Success.");
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            dataToSend.put("eroor", e.getMessage());
            LOGGER.debug("Failed to save game result. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        Map<String, Object> pageVariables = new HashMap<>();
        pageVariables.put("data", dataToSend);
        String responseContent = PageGenerator.getPage("Response", pageVariables);
        response.getWriter().println(responseContent);
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, responseContent.replace("\r\n", ""));

    }

}
