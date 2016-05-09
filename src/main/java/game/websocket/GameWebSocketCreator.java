package game.websocket;

import game.gameinternalold.GamePool;
import main.IAccountService;
import main.IGame;
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
            IGame dbService,
            String pathToMechanic,
            String pathToOutput
    ) {
        this.accountService = accountService;
        /*"static/gameMechanic.js", "static/output.txt"*/
        this.gamePool = new GamePool(pathToMechanic, pathToOutput, dbService);
    }

    @Override
    public GameWebSocket createWebSocket(ServletUpgradeRequest req, ServletUpgradeResponse resp) {
        String sessionId = req.getHttpServletRequest().getSession().getId();
        if(accountService.checkSessionExists(sessionId)) {
            return new GameWebSocket(accountService.getSession(sessionId).getUserId(), gamePool);
        }
        return null;
    }
}
