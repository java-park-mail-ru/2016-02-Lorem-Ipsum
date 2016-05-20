package game.gamemanagement.websocket;

import org.json.JSONObject;

/**
 * Created by Installed on 20.05.2016.
 */
public class OutputJSONMessagesCreator {

    public static JSONObject getMessageToStarted(GameSession session) {

        JSONObject res = new JSONObject();
        JSONObject toFirst = new JSONObject();
        JSONObject toSecond = new JSONObject();
        JSONObject dataToFirst = new JSONObject();
        JSONObject dataToSecond = new JSONObject();

        toFirst.put("action", "started");
        dataToFirst.put("enemy", session.getSecondWebSocket().getMyLogin());
        toFirst.put("data", dataToFirst);

        toSecond.put("action", "started");
        dataToSecond.put("enemy", session.getFirstWebSocket().getMyLogin());
        toSecond.put("data", dataToSecond);

        res.put("toFirst", toFirst);
        res.put("toSecond", toSecond);

        return res;
    }

    public static JSONObject getMessageToStop(GameSession session) {
        JSONObject res = new JSONObject();
        JSONObject toFirst = new JSONObject();
        JSONObject toSecond = new JSONObject();
        JSONObject dataToFirst = new JSONObject();
        JSONObject dataToSecond = new JSONObject();

        toFirst.put("action", "stop");
        dataToFirst.put("won", session.getScoreFirst() >= session.getScoreSecond());
        toFirst.put("data", dataToFirst);

        toSecond.put("action", "stop");
        dataToSecond.put("won", session.getScoreFirst() < session.getScoreSecond());
        toSecond.put("data", dataToSecond);

        res.put("toFirst", toFirst);
        res.put("toSecond", toSecond);

        return res;
    }

    public static JSONObject getMessageFreeUsers(GamePool gamePool) {
        JSONObject res = new JSONObject();
        res.put("action", "freeusers");
        res.put("data", gamePool.getFreeUsersArray());
        return res;
    }

}
