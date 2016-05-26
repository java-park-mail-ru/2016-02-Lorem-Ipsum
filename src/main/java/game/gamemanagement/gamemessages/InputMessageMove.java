package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameSession;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;

/**
 * Created by Installed on 24.05.2016.
 */
@SuppressWarnings({"CanBeFinal", "InstanceVariableNamingConvention"})
public class InputMessageMove extends MessageGame {

    private GameWebSocket socket;
    private double vx;

    public InputMessageMove(
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
        GameSession gameSession = socket.getGameSession();
        if(gameSession != null && gameSession.getStarted()) {
            gameSession.performGameAction(socket, vx);
        }
        else {
            throw new GameException(socket, "Can not perform action. Bad game session.");
        }
    }

}
