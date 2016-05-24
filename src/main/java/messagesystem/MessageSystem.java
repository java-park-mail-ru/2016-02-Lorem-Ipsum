package messagesystem;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Installed on 20.05.2016.
 */
public final class MessageSystem {
    private final Map<Address, ConcurrentLinkedQueue<Message>> messages = new HashMap<>();

    public MessageSystem() {
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
