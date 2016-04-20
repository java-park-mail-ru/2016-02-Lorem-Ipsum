package game.gameinternal;

import database.DbService;
import database.IDbService;
import game.gameinternal.instance.Instance;
import game.websocket.GameWebSocket;
import main.IGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

/**
 * Created by Installed on 17.04.2016.
 */
public class GameSession {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    Instance firstInstance;
    Instance secondInstance;

    GameWebSocket firstWebSocket;
    GameWebSocket secondWebSocket;

    Long firstUserId;
    Long secondUserId;

    Boolean isStarted;
    Boolean isInited;

    public GameSession() {
        firstInstance = null;
        secondInstance = null;
        isStarted = false;
        isInited = false;
    }

    public void init(@NotNull Long firstUserId,
                     @NotNull Long secondUserId,
                     @NotNull GameWebSocket firstWebSocket,
                     @NotNull GameWebSocket secondWebSocket)
    {
        this.firstUserId = firstUserId;
        this.secondUserId = secondUserId;
        this.firstWebSocket = firstWebSocket;
        this.secondWebSocket = secondWebSocket;
        isInited = true;
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

    public void start(String pathToMechanic, String pathToOutput,
                      JSONObject entryStartFirst, JSONObject entryStartSecond,
                      JSONObject entryMessageFirst, JSONObject entryMessageSecond,
                      JSONObject entryToStop) {
        if(!isStarted && isInited) {
            isStarted = true;
            firstInstance = new Instance(String.valueOf(firstUserId), pathToMechanic, pathToOutput, entryStartFirst);
            secondInstance = new Instance(String.valueOf(secondUserId), pathToMechanic, pathToOutput, entryStartSecond);
            firstInstance.setInstanceChecker(entryToStop, firstWebSocket);
            secondInstance.setInstanceChecker(entryToStop, secondWebSocket);
            firstWebSocket.setGameSession(this);
            secondWebSocket.setGameSession(this);
            firstWebSocket.setEnemySocket(secondWebSocket);
            secondWebSocket.setEnemySocket(firstWebSocket);
            firstWebSocket.sendMessage(entryMessageFirst);
            secondWebSocket.sendMessage(entryMessageSecond);

            LOGGER.debug("New game session. entryStartFirst: {}. entryStartSecond: {}, entryToStop: {}",
                    entryStartFirst.toString(), entryStartSecond.toString(), entryToStop.toString());
        }
    }

    public Integer stop(JSONObject entryFirst, JSONObject entrySecond, IGame dbService) {
        if(isStarted && isInited) {
            int winner = 0;
            Long scoreFirst = getScore(firstUserId);
            Long scoreSecond = getScore(secondUserId);
            if(scoreFirst < scoreSecond) {
                entryFirst.put("win", false);
                entrySecond.put("win", true);
                winner = 0;
            }
            else {
                entryFirst.put("win", true);
                entrySecond.put("win", false);
                winner = 1;
            }
            firstInstance.close();
            secondInstance.close();
            firstWebSocket.sendMessage(entryFirst);
            secondWebSocket.sendMessage(entrySecond);
            dbService.saveGameResultByUserId(firstUserId, scoreFirst, secondUserId, scoreSecond);
            isStarted = false;
            return winner;
        }
        return null;
    }

    public Long getScore(Long userId) {
        JSONObject entry = new JSONObject();
        entry.put("function", "score");
        JSONObject res = performAction(userId, entry);
        Long score = res.getLong("score");
        return score;
    }

    public JSONObject performAction(Long userId, JSONObject entry) {
        try {
            if(!isInited || !isStarted) {
                throw new Exception("bad game session");
            }
            if (userId.equals(firstUserId)) {
                return firstInstance.instanceOperator.performGet(entry);
            } else {
                if (userId.equals(secondUserId)) {
                    return secondInstance.instanceOperator.performGet(entry);
                } else {
                    throw new Exception("bad userId");
                }
            }
        }
        catch (Exception ex) {
            return new JSONObject("{\"error\" : \"failed to performAction\"}");
        }
    }
}
