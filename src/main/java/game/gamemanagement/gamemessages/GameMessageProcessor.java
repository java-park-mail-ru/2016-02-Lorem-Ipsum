package game.gamemanagement.gamemessages;

import game.GameException;
import game.gamemanagement.IGameThreadSettings;
import messagesystem.Address;
import messagesystem.IAbonent;
import messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Installed on 24.05.2016.
 */
public class GameMessageProcessor implements IAbonent, Runnable {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private final Address address = new Address();

    public GameMessageProcessor(MessageSystem messageSystem) {
        messageSystem.addService(this);
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        while (true) {
            //noinspection OverlyBroadCatchBlock
            try {
                MessageSystem.getInstance().execForAbonent(this);
                Thread.sleep(IGameThreadSettings.SERVICE_SLEEP_TIME);
            }
            catch (GameException e) {
                //noinspection UnusedAssignment
                Address outputAddress = MessageSystem.getInstance().getAddressService().getOutputProcessorAddress();
                MessageSystem.getInstance().sendMessage(
                        new OutputMessageError(
                                address,
                                address,
                                e
                        )
                );
            }
            catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }
        }
    }

}
