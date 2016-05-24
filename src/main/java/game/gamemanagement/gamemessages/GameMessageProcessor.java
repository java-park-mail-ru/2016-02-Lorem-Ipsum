package game.gamemanagement.gamemessages;

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
    private final MessageSystem messageSystem;

    public GameMessageProcessor(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
        messageSystem.addService(this);
    }

    public MessageSystem getMessageSystem() {
        return messageSystem;
    }

    @Override
    public Address getAddress() {
        return address;
    }

    @Override
    public void run() {
        while (true) {
            try {
                messageSystem.execForAbonent(this);
                Thread.sleep(IGameThreadSettings.SERVICE_SLEEP_TIME);
            }
            catch (Exception e) {
                LOGGER.debug(e.getMessage());
            }
        }
    }

}
