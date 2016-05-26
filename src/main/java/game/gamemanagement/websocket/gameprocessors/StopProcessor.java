package game.gamemanagement.websocket.gameprocessors;

import game.GameException;
import game.gamemanagement.websocket.GameSession;
import game.gameprimitives.physicalinstance.IStopProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Installed on 20.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class StopProcessor implements IStopProcessor {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private GameSession gameSession;

    public StopProcessor(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public void stop() {
        try {
            gameSession.getGamePool().stopGame(
                    gameSession.getFirstWebSocket(),
                    gameSession.getSecondWebSocket()
            );
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

}
