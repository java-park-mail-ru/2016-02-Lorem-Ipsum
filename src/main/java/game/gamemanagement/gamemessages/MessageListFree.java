package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;

/**
 * Created by Installed on 24.05.2016.
 */
public class MessageListFree extends MessageToGame {

    private GameWebSocket socket;

    public MessageListFree(
            Address from,
            Address to,
            GameWebSocket socket
    ) {
        super(from, to);
        this.socket = socket;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.getHandlers().gameGetFree();
    }

}
