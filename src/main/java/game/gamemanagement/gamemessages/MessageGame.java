package game.gamemanagement.gamemessages;

import game.GameException;
import messagesystem.Address;
import messagesystem.IAbonent;
import messagesystem.Message;

/**
 * Created by Installed on 24.05.2016.
 */
public abstract class MessageGame extends Message {

    public MessageGame(Address from, Address to) {
        super(from, to);
    }

    @Override
    public void exec(IAbonent abonent) throws GameException {
        if(abonent instanceof GameMessageProcessor) {
            execInternal((GameMessageProcessor) abonent);
        }
    }

    @SuppressWarnings("UnusedParameters")
    protected abstract void execInternal(GameMessageProcessor processor) throws GameException;

}
