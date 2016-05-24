package game.gamemanagement.websocket.gameprocessors;

import game.GameException;
import game.gamemanagement.websocket.GameSession;
import game.gameprimitives.physicalinstance.IStopProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public class StopProcessor implements IStopProcessor {

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

        }
    }

}
