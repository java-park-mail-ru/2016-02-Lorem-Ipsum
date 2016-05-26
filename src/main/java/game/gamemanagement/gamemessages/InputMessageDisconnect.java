package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameSession;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;
import messagesystem.MessageSystem;

/**
 * Created by Installed on 24.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class InputMessageDisconnect extends MessageGame {

    private GameWebSocket socket;

    public InputMessageDisconnect(
            Address from,
            Address to,
            GameWebSocket socket
    ) {
        super(from, to);
        this.socket = socket;
    }

    @Override
    protected void execInternal(GameMessageProcessor processor) throws GameException {
        socket.stop();
        GameSession gameSession = socket.getGameSession();
        Address addressInput = MessageSystem.getInstance().
                getAddressService().
                getInputProcessorAddress();
        Address addressOutput = MessageSystem.getInstance().
                getAddressService().
                getOutputProcessorAddress();
        MessageSystem.getInstance().sendMessage(
                new OutputMessageStart(
                        addressInput,
                        addressOutput,
                        gameSession
                )
        );
    }

}
