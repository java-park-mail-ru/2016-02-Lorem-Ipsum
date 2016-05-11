package game.tmpgame;


import game.gameinternalold.GameException;
import main.IGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Installed on 17.04.2016.
 */
public class GameSession {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");

    GameState gameState;

    GameWebSocket firstWebSocket;
    GameWebSocket secondWebSocket;

    Long firstUserId;
    Long secondUserId;

    Boolean isStarted;
    Boolean isInited;

    public GameSession() {
        isStarted = false;
        isInited = false;
    }

    @SuppressWarnings("ParameterHidesMemberVariable")
    public void init(@NotNull Long firstUserId,
                     @NotNull Long secondUserId,
                     @NotNull GameWebSocket firstWebSocket,
                     @NotNull GameWebSocket secondWebSocket)
    {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.firstWebSocket = firstWebSocket;
        this.secondWebSocket = secondWebSocket;
        gameState = new GameState(4, 20);
        isInited = true;
    }

    public Boolean getStarted() {
        return isStarted;
    }

    public void setStarted(Boolean started) {
        isStarted = started;
    }

    public Boolean getIsInited() { return isInited; }

    public void setIsInited(Boolean inited) {
        this.isInited = inited;
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

    public void setFirstUserId(Long userId) {
        this.firstUserId = userId;
    }

    public Long getFirstUserId() {
        return firstUserId;
    }

    public void setSecondUserId(Long userId) {
        this.secondUserId = userId;
    }

    public Long getSecondUserId() {
        return secondUserId;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void start() throws GameException {
        if(!isStarted && isInited) {
            isStarted = true;
            firstWebSocket.setGameSession(this);
            secondWebSocket.setGameSession(this);
            firstWebSocket.setEnemySocket(secondWebSocket);
            secondWebSocket.setEnemySocket(firstWebSocket);
            firstWebSocket.sendMessage(getMessageToStart(0));
            secondWebSocket.sendMessage(getMessageToStart(1));

            LOGGER.debug("New game session. firstId: {}. secondId: {}",
                    firstUserId, secondUserId);
        }
        else {
            throw new GameException("Unable to start game, improper condition.");
        }
    }

    public JSONObject getMessageToStart(int toWhom) {
        JSONObject res = new JSONObject();
        res.put("type", "start");
        JSONObject content = new JSONObject();
        if(toWhom == 0)
            content.put("enemy", firstUserId);
        else
            content.put("enemy", secondUserId);
        res.put("content", content);
        return res;
    }

    public void stop() throws GameException {
        if(isStarted && isInited) {
            firstWebSocket.setEnemyFound(false);
            secondWebSocket.setEnemyFound(false);
            firstWebSocket.setGameSession(null);
            secondWebSocket.setGameSession(null);
            firstWebSocket.sendMessage(getMessageToStop(0));
            secondWebSocket.sendMessage(getMessageToStop(1));
        }
        else {
            throw new GameException("Unable to stop game, improper condition.");
        }
    }

    public JSONObject getMessageToStop(int toWhom) {
        JSONObject res = new JSONObject();
        res.put("type", "stop");
        JSONObject content = new JSONObject();
        if(toWhom == 0)
            content.put("enemy", firstUserId);
        else
            content.put("enemy", secondUserId);
        res.put("content", content);
        return res;
    }

    public JSONArray changeGameState(JSONArray mtrJSON) {
        return gameState.conjunct(mtrJSON);
    }

}
