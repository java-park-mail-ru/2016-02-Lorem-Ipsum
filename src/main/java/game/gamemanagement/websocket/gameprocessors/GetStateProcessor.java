package game.gamemanagement.websocket.gameprocessors;

import game.gamemanagement.gamemessages.OutputMessageGetState;
import game.gamemanagement.websocket.GameSession;
import game.gameprimitives.physicalinstance.IGetStateProcessor;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.json.JSONObject;

/**
 * Created by Installed on 20.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class GetStateProcessor implements IGetStateProcessor {

    private GameSession gameSession;

    public GetStateProcessor(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
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
        resSecond.put("action", "update_state");
        resSecond.put("state", secondJSON);


        Address addressInput = MessageSystem.getInstance().
                getAddressService().
                getInputProcessorAddress();
        Address addressOutput = MessageSystem.getInstance().
                getAddressService().
                getOutputProcessorAddress();
        MessageSystem.getInstance().sendMessage(
                new OutputMessageGetState(
                        addressInput,
                        addressOutput,
                        gameSession.getFirstWebSocket(),
                        resFirst
                )
        );
        MessageSystem.getInstance().sendMessage(
                new OutputMessageGetState(
                        addressInput,
                        addressOutput,
                        gameSession.getSecondWebSocket(),
                        resSecond
                )
        );
    }
}
