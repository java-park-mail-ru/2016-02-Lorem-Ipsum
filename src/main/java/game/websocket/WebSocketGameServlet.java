package game.websocket;

import game.gameinternal.GamePool;
import main.IAccountService;
import main.IGame;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import javax.servlet.annotation.WebServlet;

/**
 * Created by Installed on 18.04.2016.
 */
@WebServlet(name = "WebSocketGameServlet", urlPatterns = {"/gameplay"})
public class WebSocketGameServlet extends WebSocketServlet {
    private final static int IDLE_TIME = 60 * 100000;
    private IAccountService accountService;
    private IGame dbService;
    private String pathToMechanic;
    private String pathToOutput;

    public WebSocketGameServlet(
            IAccountService accountService,
            IGame dbService,
            String pathToMechanic,
            String pathToOutput
    ) {
        this.accountService = accountService;
        this.dbService = dbService;
        this.pathToMechanic = pathToMechanic;
        this.pathToOutput = pathToOutput;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(IDLE_TIME);
        factory.setCreator(new GameWebSocketCreator(accountService, dbService, pathToMechanic, pathToOutput));
    }
}
