package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;

/**
 * Created by Installed on 24.05.2016.
 */
public class MessageError extends MessageToGame {

    private GameWebSocket socket;
    private GameException ex;

    public MessageError(
            Address from,
            Address to,
            GameWebSocket socket,
            GameException ex
    ) {
        super(from, to);
        this.socket = socket;
        this.ex = ex;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.getHandlers().gameError(ex);
    }

}
