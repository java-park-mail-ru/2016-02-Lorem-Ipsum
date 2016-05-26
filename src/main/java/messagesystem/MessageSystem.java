package messagesystem;

import game.gamemanagement.gamemessages.GameMessageProcessor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Installed on 20.05.2016.
 */
public final class MessageSystem {
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();
    private final AddressService addressService = new AddressService();

    @SuppressWarnings("RedundantNoArgConstructor")
    private MessageSystem() {
        GameMessageProcessor messageInputProcessor = new GameMessageProcessor(this);
        GameMessageProcessor messageOuputProcessor = new GameMessageProcessor(this);
        addressService.registerInputProcessor(messageInputProcessor);
        addressService.registerOutputProcessor(messageOuputProcessor);
        Thread messageInputProcessorThread = new Thread(messageInputProcessor);
        Thread messageOutputProcessorThread = new Thread(messageOuputProcessor);
        messageInputProcessorThread.setDaemon(true);
        messageOutputProcessorThread.setDaemon(true);
        messageInputProcessorThread.setName("GameInputMessageProcessor");
        messageOutputProcessorThread.setName("GameOutputMessageProcessor");
        messageInputProcessorThread.start();
        messageOutputProcessorThread.start();
    }

    private static class MessageSystemHolder {
        private static final MessageSystem INSTANCE = new MessageSystem();
    }

    public static MessageSystem getInstance() {
        return MessageSystemHolder.INSTANCE;
    }

    public AddressService getAddressService() {
        return addressService;
    }

    public void addService(IAbonent abonent) {
        messages.put(abonent.getAddress(), new ConcurrentLinkedQueue<>());
    }

    public void sendMessage(Message message) {
        messages.get(message.getTo()).add(message);
    }

    public void execForAbonent(IAbonent abonent) throws Exception {
        ConcurrentLinkedQueue<Message> queue = messages.get(abonent.getAddress());
        while (!queue.isEmpty()) {
            Message message = queue.poll();
            message.exec(abonent);
        }
    }
}
