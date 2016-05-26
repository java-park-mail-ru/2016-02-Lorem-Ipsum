package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.websocket.GameWebSocket;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Installed on 26.05.2016.
 */
public class InputMessageError extends MessageGame {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private final GameWebSocket socket;
    @SuppressWarnings("InstanceVariableNamingConvention")
    private final GameException ex;

    public InputMessageError(
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
        LOGGER.debug(ex.getMessage());

        Address addressInput = MessageSystem.getInstance().
                getAddressService().
                getInputProcessorAddress();
        Address addressOutput = MessageSystem.getInstance().
                getAddressService().
                getOutputProcessorAddress();
        MessageSystem.getInstance().sendMessage(
                new OutputMessageError(
                        addressInput,
                        addressOutput,
                        ex
                )
        );
    }

}
