package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;

/**
 * Created by Installed on 24.05.2016.
 */
public class MessageMove extends MessageToGame {

    private GameWebSocket socket;
    private double vx;

    public MessageMove(
            Address from,
            Address to,
            GameWebSocket socket,
            double vx
    ) {
        super(from, to);
        this.socket = socket;
        this.vx = vx;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.getHandlers().gameAction(socket, vx);
    }

}
