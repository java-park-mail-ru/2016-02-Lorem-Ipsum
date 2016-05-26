package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GamePool;
import game.gamemanagement.websocket.GameSession;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;
import messagesystem.MessageSystem;

/**
 * Created by Installed on 24.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class InputMessageStart extends MessageGame {

    private GameWebSocket socket;
    private String enemyLogin;

    public InputMessageStart(
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

        GamePool gamePool = socket.getGamePool();
        GameWebSocket enemysocket = gamePool.getFreeUserByLogin(enemyLogin);
        if(enemysocket != null)
            gamePool.startGame(socket, enemysocket);
        else
            throw new GameException(socket, "User not found or not free.");


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
