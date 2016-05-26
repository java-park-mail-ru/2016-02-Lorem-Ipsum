package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameSession;
import game.gamemanagement.websocket.OutputJSONMessagesCreator;
import messagesystem.Address;
import org.json.JSONObject;

/**
 * Created by Installed on 26.05.2016.
 */
public class OutputMessageStart extends MessageGame {

    private final GameSession gameSession;

    public OutputMessageStart(
            Address from,
            Address to,
            GameSession gameSession
    ) {
        super(from, to);
        this.gameSession = gameSession;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        JSONObject toSend = OutputJSONMessagesCreator.getMessageToStarted(gameSession);
        gameSession.getFirstWebSocket().sendMessage(
                toSend.getJSONObject("toFirst")
        );
        gameSession.getSecondWebSocket().sendMessage(
                toSend.getJSONObject("toSecond")
        );
    }

}
