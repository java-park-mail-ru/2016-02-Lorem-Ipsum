package frontend.utils.reqproc;

import database.datasets.GameResultDataSet;
import database.exceptions.gmrsexceptions.GetBestResultsException;
import main.IAccountService;
import main.IGame;
import org.json.JSONArray;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by Installed on 29.05.2016.
 */
public class GetBestResultsProcessor extends RequestProcessor {

    private final IGame gameService;
    private JSONArray bestResultsArray;

    public GetBestResultsProcessor(
            HttpServletRequest request,
            HttpServletResponse response,
            IAccountService accountService,
            IGame gameService
    ) {
        super(request, response, accountService);
        this.gameService = gameService;
    }

    @Override
    protected void verify() {

    }

    @Override
    protected void execInternal()
        throws GetBestResultsException
    {
        List<GameResultDataSet> gameResultDataSets = gameService.getBestResults(7);
        bestResultsArray = new JSONArray();
        for(GameResultDataSet gr: gameResultDataSets) {
            bestResultsArray.put(gr.toJSONObject());
        }
        statusCode = HttpServletResponse.SC_OK;
    }

    @Override
    public void execute(int failureCode) {
        try {
            verify();
            execInternal();
            response.setStatus(statusCode);
            response.setContentType("application/json");
            response.getWriter().println(bestResultsArray.toString());
            LOGGER.debug("Success.");
        } catch (GetBestResultsException e) {
            LOGGER.debug("Unable to get best results. Reason: {}", e.getMessage());
        }
        catch (IOException e) {
            LOGGER.debug("Cannot write response. Reason: {}", e.getMessage());
        }
    }

}
