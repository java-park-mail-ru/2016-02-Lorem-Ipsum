package game.websocket;

import database.IDbService;
import game.gameinternal.GamePool;
import game.gameinternal.GameSession;
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
public class GameWebSocket {
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
        this.session = session;
        this.gamePool.connectUser(myId, this);
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        JSONObject input = new JSONObject(data);
        JSONObject output = new JSONObject();
        String type = "none";
        if(input.has("type")) {
            type = input.getString("type");
        }
        if(type.equals("gameAction") && gameSession != null && gameSession.getStarted()) {
            output = gameSession.performAction(myId, input);
            sendMessage(output);
        }
        if(type.equals("gameStart") && input.has("enemyId")) {
            Long enemyId = input.getLong("enemyId");
            gamePool.startGame(myId, enemyId);
        }
        if(type.equals("gameStop")) {
            Long enemyId = enemySocket.getMyId();
            gamePool.startGame(myId, enemyId);
        }
        if(type.equals("gameInfo")) {
            JSONArray arr = gamePool.getConnectedUsers();
            JSONObject res = new JSONObject();
            res.put("users", arr);
            sendMessage(res);
        }
    }

    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        Long enemyId = enemySocket.getMyId();
        gamePool.startGame(myId, enemyId);
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

    public void sendMessage(JSONObject output) {
        if(output == null)
            return;
        try  {
            if(session != null && session.isOpen()) {
                session.getRemote().sendString(output.toString());
            }
        } catch (IOException e) {
            LOGGER.debug(e.getMessage());
        }
    }
}
