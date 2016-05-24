package game.gamemanagement.websocket.gameprocessors;

import game.gamemanagement.websocket.GameSession;
import game.gameprimitives.physicalinstance.IGetStateProcessor;
import org.json.JSONObject;

/**
 * Created by Installed on 20.05.2016.
 */
public class GetStateProcessor implements IGetStateProcessor {

    private GameSession gameSession;

    public GetStateProcessor(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public void process(JSONObject state) {
        JSONObject firstJSON = state.getJSONObject("toFirst");
        JSONObject secondJSON = state.getJSONObject("toSecond");

        firstJSON.put("your_score", gameSession.getScoreFirst());
        firstJSON.put("enemy_score", gameSession.getScoreSecond());
        secondJSON.put("your_score", gameSession.getScoreSecond());
        secondJSON.put("enemy_score", gameSession.getScoreFirst());

        JSONObject resFirst = new JSONObject();
        resFirst.put("action", "update_state");
        resFirst.put("state", firstJSON);

        JSONObject resSecond = new JSONObject();
        resFirst.put("action", "update_state");
        resFirst.put("state", secondJSON);

        gameSession.getFirstWebSocket().sendMessage(resFirst);
        gameSession.getSecondWebSocket().sendMessage(resSecond);
    }
}
