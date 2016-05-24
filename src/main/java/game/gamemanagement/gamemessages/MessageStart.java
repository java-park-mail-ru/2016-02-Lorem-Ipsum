package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;

/**
 * Created by Installed on 24.05.2016.
 */
public class MessageStart extends MessageToGame {

    private GameWebSocket socket;
    private String enemyLogin;

    public MessageStart(
            Address from,
            Address to,
            GameWebSocket socket,
            String enemyLogin
    ) {
        super(from, to);
        this.socket = socket;
        this.enemyLogin = enemyLogin;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.getHandlers().gameStart(socket, enemyLogin);
    }

}
