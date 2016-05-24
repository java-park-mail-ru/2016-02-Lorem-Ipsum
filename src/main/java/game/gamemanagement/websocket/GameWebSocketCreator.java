package game.gamemanagement.websocket;

import game.gamemanagement.gamemessages.GameMessageProcessor;
import main.IAccountService;
import main.IGame;
import main.UserProfile;
import messagesystem.MessageSystem;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * Created by Installed on 18.04.2016.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private final IAccountService accountService;
    private final GamePool gamePool;
    private final MessageSystem messageSystem;
    private final GameMessageProcessor messageProcessor;
    private final Thread messageProcessorThread;

    public GameWebSocketCreator(
            IAccountService accountService,
            IGame dbService,
            MessageSystem messageSystem
    ) {
        this.accountService = accountService;
        this.gamePool = new GamePool(dbService);
        this.messageSystem = messageSystem;
        this.messageProcessor = new GameMessageProcessor(this.messageSystem);
        messageProcessorThread = new Thread(messageProcessor);
        messageProcessorThread.setDaemon(true);
        messageProcessorThread.setName("GameMessageProcessor");
        messageProcessorThread.start();
    }

    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        if(accountService.checkSessionExists(sessionId)) {
            UserProfile profile = accountService.getSession(sessionId);
            return new GameWebSocket(
                    profile.getUserId(),
                    profile.getLogin(),
                    gamePool,
                    messageProcessor
            );
        }
        return null;
    }
}
