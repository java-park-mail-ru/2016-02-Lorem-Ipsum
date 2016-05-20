package game.gamemanagement.websocket;

import main.IAccountService;
import main.IGame;
import main.UserProfile;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

/**
 * Created by Installed on 18.04.2016.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private final IAccountService accountService;
    private final GamePool gamePool;

    public GameWebSocketCreator(
            IAccountService accountService,
            IGame dbService
    ) {
        this.accountService = accountService;
        this.gamePool = new GamePool(dbService);
    }

    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        if(accountService.checkSessionExists(sessionId)) {
            UserProfile profile = accountService.getSession(sessionId);
            return new GameWebSocket(
                    profile.getUserId(),
                    profile.getLogin(),
                    gamePool
            );
        }
        return null;
    }
}
