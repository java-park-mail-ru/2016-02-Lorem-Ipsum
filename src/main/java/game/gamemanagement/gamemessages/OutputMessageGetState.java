package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;
import org.json.JSONObject;

/**
 * Created by Installed on 26.05.2016.
 */
public class OutputMessageGetState extends MessageGame {

    private final GameWebSocket socket;
    private final JSONObject messageJSON;

    public OutputMessageGetState(
            Address from,
            Address to,
            GameWebSocket socket,
            JSONObject messageJSON
    ) {
        super(from, to);
        this.socket = socket;
        this.messageJSON = messageJSON;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.sendMessage(messageJSON);
    }

}
