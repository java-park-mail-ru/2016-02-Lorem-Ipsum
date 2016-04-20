package game.websocket;

import database.IDbService;
import game.gameinternal.GameException;
import game.gameinternal.GamePool;
import game.gameinternal.GameSession;
import game.gameinternal.instance.Stopable;
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

import java.io.IOException;

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

    public GameWebSocket(Long myId, GamePool gamePool) {
        this.myId = myId;
        this.gamePool = gamePool;
        this.gameSession = null;
        this.enemySocket = null;
    }

    @OnWebSocketConnect
    public void onOpen(@NotNull Session session) {
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
        JSONObject output = new JSONObject();
        try {
            String type;
            if (input.has("type")) {
                type = input.getString("type");
            }
            else
                throw new GameException("Type parameter missing.");

            if (type.equals("gameAction")) {
                if(gameSession != null && gameSession.getStarted()) {
                    output = gameSession.performAction(myId, input);
                    sendMessage(output);
                    if (output.has("sendToEnemy")) {
                        if (output.getBoolean("sendToEnemy")) {
                            enemySocket.sendMessage(output);
                        }
                    }
                }
                else {
                    throw new GameException("Unable to perform action, improper condition.");
                }
                return;
            }
            if (type.equals("gameStart") && input.has("enemyId")) {
                Long enemyId = input.getLong("enemyId");
                gamePool.startGame(myId, enemyId);
                return;
            }
            if (type.equals("gameStop")) {
                stop();
                return;
            }
            if (type.equals("gameInfo")) {
                JSONArray arr = gamePool.getFreeUsersArray();
                JSONObject res = new JSONObject();
                res.put("users", arr);
                sendMessage(res);
                return;
            }
            throw new GameException("Unknown action.");
        }//try
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
            JSONObject errJSON = new JSONObject();
            errJSON.put("error", ex.getMessage());
            sendMessage(errJSON);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        try {
            gamePool.disconnectUser(myId);
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
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

    public void sendMessageWithSession(Session session, JSONObject output) {
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

    public void stop() throws GameException {
        Long enemyId = enemySocket.getMyId();
        gamePool.stopGame(myId, enemyId);
    }
}
