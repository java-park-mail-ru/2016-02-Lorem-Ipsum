package game.gamemanagement.websocket.gameprocessors;

import game.gamemanagement.websocket.GameSession;
import game.gameprimitives.physicalinstance.IScoreProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public class ScoreProcessor implements IScoreProcessor {

    private GameSession gameSession;
    private final int SCORE = 100;

    public ScoreProcessor(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    @Override
    public void scoresFirst() {
        gameSession.setScoreFirst(gameSession.getScoreFirst() + SCORE);
    }

    @Override
    public void scoresSecond() {
        gameSession.setScoreSecond(gameSession.getScoreSecond() + SCORE);
    }

}
