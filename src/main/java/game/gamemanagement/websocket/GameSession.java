package game.gamemanagement.websocket;


import game.GameException;
import game.gamemanagement.instance.GameInstance;
import game.gamemanagement.instance.IGameInstance;
import game.gamemanagement.websocket.gameprocessors.GetStateProcessor;
import game.gamemanagement.websocket.gameprocessors.ScoreProcessor;
import game.gamemanagement.websocket.gameprocessors.StopProcessor;
import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Created by Installed on 17.04.2016.
 */
public class GameSession {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");

    IGameInstance gameInstance;

    GamePool gamePool;

    GameWebSocket firstWebSocket;
    GameWebSocket secondWebSocket;

    Boolean isStarted;

    Long scoreFirst;
    Long scoreSecond;

    public GameSession(
            @NotNull GamePool gamePool,
            @NotNull GameWebSocket firstWebSocket,
            @NotNull GameWebSocket secondWebSocket
    ) {
        isStarted = false;
        this.firstWebSocket = firstWebSocket;
        this.secondWebSocket = secondWebSocket;
        this.scoreFirst = 0L;
        this.scoreSecond = 0L;
        this.gamePool = gamePool;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public GameWebSocket getFirstWebSocket() {
        return firstWebSocket;
    }

    public void setFirstWebSocket(GameWebSocket firstWebSocket) {
        this.firstWebSocket = firstWebSocket;
    }

    public GameWebSocket getSecondWebSocket() {
        return secondWebSocket;
    }

    public void setSecondWebSocket(GameWebSocket secondWebSocket) {
        this.secondWebSocket = secondWebSocket;
    }

    public void setScoreFirst(Long scoreFirst) {this.scoreFirst = scoreFirst;}

    public Long getScoreFirst() {return scoreFirst;}

    public void setScoreSecond(Long scoreSecond) {this.scoreSecond = scoreSecond;}

    public Long getScoreSecond() {return scoreSecond;}

    public GamePool getGamePool() {return gamePool;}

    public void start() throws GameException {

        if(!isStarted) {

            isStarted = true;
            firstWebSocket.setGameSession(this);
            secondWebSocket.setGameSession(this);
            firstWebSocket.setEnemySocket(secondWebSocket);
            secondWebSocket.setEnemySocket(firstWebSocket);
            JSONObject toSend = OutputJSONMessagesCreator.getMessageToStarted(this);
            firstWebSocket.sendMessage(
                    toSend.getJSONObject("toFirst")
            );
            secondWebSocket.sendMessage(
                    toSend.getJSONObject("toSecond")
            );
            gameInstance = new GameInstance(
                    new ScoreProcessor(this),
                    new GetStateProcessor(this),
                    new StopProcessor(this)
            );

            LOGGER.debug(
                    "New game session. firstId: {}. secondId: {}",
                    firstWebSocket.getMyId(),
                    secondWebSocket.getMyId()
            );
        }
        else {
            throw new GameException("Unable to start game, improper condition.");
        }
    }

    public void stop() throws GameException {
        if(isStarted) {

            firstWebSocket.setGameSession(null);
            secondWebSocket.setGameSession(null);
            gameInstance.performStop();
            JSONObject toSend = OutputJSONMessagesCreator.getMessageToStop(this);
            firstWebSocket.sendMessage(
                    toSend.getJSONObject("toFirst")
            );
            secondWebSocket.sendMessage(
                    toSend.getJSONObject("toSecond")
            );
        }
        else {
            throw new GameException("Unable to stop game, improper condition.");
        }
    }

    public void performGameAction(
            GameWebSocket player,
            double vx
    ) throws GameException
    {
        if(player.getMyId().equals(firstWebSocket.getMyId()))
            gameInstance.performRedirrectPlatform(
                    IPhysicalInstanceProcessor.NUM_PLAYER.FIRST,
                    vx
            );
        else if(player.getMyId().equals(secondWebSocket.getMyId()))
            gameInstance.performRedirrectPlatform(
                    IPhysicalInstanceProcessor.NUM_PLAYER.SECOND,
                    vx
            );
        else throw new GameException("Unable to determine the message detination");
    }

}
