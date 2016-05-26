package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import game.gamemanagement.websocket.OutputJSONMessagesCreator;
import messagesystem.Address;
import org.json.JSONObject;

/**
 * Created by Installed on 24.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class OutputMessageListFree extends MessageGame {

    private GameWebSocket socket;

    public OutputMessageListFree(
            Address from,
            Address to,
            GameWebSocket socket
    ) {
        super(from, to);
        this.socket = socket;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        JSONObject res = OutputJSONMessagesCreator.getMessageFreeUsers(socket.getGamePool());
        socket.sendMessage(res);
    }

}
