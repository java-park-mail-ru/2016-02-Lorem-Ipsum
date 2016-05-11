package game.tmpgame;

import game.gameinternalold.GameException;
import game.gameinternalold.instance.Stopable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.Set;

/**
 * Created by Installed on 17.04.2016.
 */
@WebSocket
public class GameWebSocket implements Stopable {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private Long myId;
    private Session session;
    private GamePool gamePool;
    private GameSession gameSession;
    private GameWebSocket enemySocket;
    private final Handlers handlers;
    boolean enemyFound;

    public GameWebSocket(Long myId, GamePool gamePool) {
        this.myId = myId;
        this.gamePool = gamePool;
        this.gameSession = null;
        this.enemySocket = null;
        this.handlers = new Handlers();
        this.enemyFound = false;
    }

    @OnWebSocketConnect
    public void onOpen(@SuppressWarnings("ParameterHidesMemberVariable") @NotNull Session session) {
        try {
            this.session = session;
            this.gamePool.connectUser(myId, this);
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONObject input = new JSONObject(data);
        //noinspection OverlyBroadCatchBlock
        try {
            tryToFindSomeEnemy();
            handlers.gameAction(input);
        }//try
        catch (GameException ex) {
            handlers.gameError(ex);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    public void tryToFindSomeEnemy() throws GameException {
        if(enemyFound == false) {
            Long enemyId = gamePool.getSomeFreePlayer(myId);
            if (enemyId != -1) {
                handlers.gameStart(enemyId);
                enemyFound = true;
                enemySocket.setEnemyFound(true);
            }
        }
    }

    @SuppressWarnings("UnusedParameters")
    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        try {
            if(gameSession != null && gameSession.getStarted()) {
                stop();
            }
            gamePool.disconnectUser(myId);
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    /***************************************************************/
    private class Handlers {

        public void gameAction(JSONObject input) throws GameException {
            if(gameSession != null && gameSession.getStarted()) {
                JSONArray matrix = input.getJSONArray("blocks");
                JSONArray newMatrix = gameSession.changeGameState(matrix);
                input.put("blocks", newMatrix);
                JSONObject toEnemy = new JSONObject();
                toEnemy.put("another_platform", input.getJSONObject("your_platform"));
                toEnemy.put("another_ball", input.getJSONObject("your_ball"));
                toEnemy.put("blocks", newMatrix);
                sendMessage(input);
                enemySocket.sendMessage(toEnemy);
            }
            else {
                sendMessage(input);;
            }
        }

        public void gameStart(Long enemyId) throws GameException {
            gamePool.startGame(myId, enemyId);
        }

        public void gameInfo() {
            JSONArray arr = gamePool.getFreeUsersArray();
            JSONObject res = new JSONObject();
            res.put("freeUsers", arr);
            sendMessage(res);
        }

        public void gameError(GameException ex) {
            LOGGER.debug(ex.getMessage());
            JSONObject errJSON = new JSONObject();
            errJSON.put("error", ex.getMessage());
            sendMessage(errJSON);
        }

    }
    /***************************************************************/
    public void sendMessageWithSession(@SuppressWarnings("ParameterHidesMemberVariable") Session session, JSONObject output) {
        if(output == null)
            return;
        try  {
            if(session != null && session.isOpen()) {
                session.getRemote().sendString(output.toString(4));
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }

    @Override
    public void stop() throws GameException {
        @SuppressWarnings("CallToSimpleGetterFromWithinClass") Long enemyId = enemySocket.getMyId();
        gamePool.stopGame(myId, enemyId);
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    public GameSession getGameSession() {
        return gameSession;
    }

    public void setGamePool(GamePool gamePool) {
        this.gamePool = gamePool;
    }

    public GamePool getGamePool() {
        return gamePool;
    }

    public void setEnemySocket(GameWebSocket gameWebSocket) {
        this.enemySocket = gameWebSocket;
    }

    public GameWebSocket getEnemySocket() {
        return enemySocket;
    }

    public Long getMyId() {
        return myId;
    }

    public void setMyId(Long myId) {
        this.myId = myId;
    }

    public void setSession(Session session) {this.session = session;}

    public Session getSession() {return session;}

    public void sendMessage(JSONObject output) {
        sendMessageWithSession(session, output);
    }

    public boolean getEnemyFound() {return enemyFound;}

    public void setEnemyFound(boolean enemyFound) { this.enemyFound = enemyFound; }
}
