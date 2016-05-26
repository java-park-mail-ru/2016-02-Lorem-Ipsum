package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;
import org.json.JSONObject;

/**
 * Created by Installed on 24.05.2016.
 */
@SuppressWarnings({"CanBeFinal", "InstanceVariableNamingConvention"})
public class OutputMessageError extends MessageGame {

    private GameWebSocket socket;
    private GameException ex;

    public OutputMessageError(
            Address from,
            Address to,
            GameException ex
    ) {
        super(from, to);
        this.ex = ex;
        this.socket = ex.getSocket();
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        JSONObject errJSON = new JSONObject();
        errJSON.put("action", "error");
        JSONObject data = new JSONObject();
        data.put("message", ex.getMessage());
        errJSON.put("data", data);
        socket.sendMessage(errJSON);
    }

}
