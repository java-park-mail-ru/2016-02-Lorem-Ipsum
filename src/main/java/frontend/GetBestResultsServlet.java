package frontend;

import database.datasets.GameResultDataSet;
import frontend.utils.RequestParser;
import main.IGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import templater.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Installed on 03.04.2016.
 */
public class GetBestResultsServlet extends HttpServlet {

    private final IGame gameService;
    public static final String REQUEST_URI = "/score";

    public static final Logger LOGGER = LogManager.getLogger(SaveGameScoreServlet.class);

    public GetBestResultsServlet(IGame accountService) {
        this.gameService = accountService;
    }

    @Override
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response) throws ServletException, IOException {

        String sessionId = request.getSession().getId();

        LOGGER.debug("GetBestResultServlet request got.");

        int statusCode;
        JSONArray bestResultsArray = null;

        try {
            List<GameResultDataSet> gameResultDataSets = gameService.getBestResults(7);
            bestResultsArray = new JSONArray();
            for(GameResultDataSet gr: gameResultDataSets) {
                bestResultsArray.put(gr.toJSONObject());
            }
            statusCode = HttpServletResponse.SC_OK;
            response.getWriter().println(bestResultsArray.toString());
            LOGGER.debug("Success.");
        }
        catch (Exception e) {
            statusCode = HttpServletResponse.SC_FORBIDDEN;
            LOGGER.debug("Failed to get best game results. SessionId: {}. Reason: {}", sessionId, e.getMessage());
        }

        response.setStatus(statusCode);
        response.setContentType("application/json");
        LOGGER.debug("Servlet finished with code {}, response body: {}", statusCode, bestResultsArray == null ? "" :
                bestResultsArray.toString());

    }
}
