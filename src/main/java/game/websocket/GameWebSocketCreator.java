package game.websocket;

import database.IDbService;
import game.gameinternal.GamePool;
import main.IAccountService;
import main.IGame;
import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Installed on 18.04.2016.
 */
public class GameWebSocketCreator implements WebSocketCreator {
    private IAccountService accountService;
    private GamePool gamePool;

    public GameWebSocketCreator(IAccountService accountService,
                                IGame dbService) {
        this.accountService = accountService;
        this.gamePool = new GamePool("static/gameMechanic.js", "static/output.txt", dbService);
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
