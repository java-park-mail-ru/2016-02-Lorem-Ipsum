package game.gamemanagement.websocket;

import game.Stopable;
import game.GameException;
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
    private String myLogin;
    private Session session;
    private GamePool gamePool;
    private GameSession gameSession;
    private GameWebSocket enemySocket;
    private Handlers handlers = new Handlers();

    public GameWebSocket(
            Long myId,
            String myLogin,
            GamePool gamePool
    ) {
        this.myId = myId;
        this.myLogin = myLogin;
        this.gamePool = gamePool;
        this.gameSession = null;
        this.enemySocket = null;
    }

    @OnWebSocketConnect
    public void onOpen(@SuppressWarnings("ParameterHidesMemberVariable")
                           @NotNull Session session) {
        try {
            this.session = session;
            this.gamePool.connectUser(this);
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(String dataInput) {
        JSONObject input = new JSONObject(dataInput);
        //noinspection OverlyBroadCatchBlock
        try {
            String action = input.getString("action");
            switch (action) {
                case "start" : {
                    JSONObject data = input.getJSONObject("data");
                    String enemyLogin = data.getString("enemy");
                    handlers.gameStart(this, enemyLogin);
                }
                break;
                case "left" : {
                    handlers.gameAction(this, -5);
                }
                break;
                case "right" : {
                    handlers.gameAction(this, 5);
                }
                break;
                case "stop" : {
                    handlers.gameAction(this, 0);
                }
                break;
                case "disconnect" : {
                    this.stop();
                }
                break;
                case "freeusers" :
                case "connect" : {
                    handlers.gameGetFree();
                }
                break;
            }
        }//try
        catch (GameException ex) {
            handlers.gameError(ex);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    @SuppressWarnings("UnusedParameters")
    @OnWebSocketClose
    public void onClose(int closeCode, String closeReason) {
        try {
            this.stop();
            gamePool.disconnectUser(this);
        }
        catch (GameException ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    /***************************************************************/
    private class Handlers {

        public void gameAction(GameWebSocket player, double vx) throws GameException {
            if(gameSession != null && gameSession.getStarted()) {
                gameSession.performGameAction(player, vx);
            }
            else {
                throw new GameException("Can not perform action. Bad game session.");
            }
        }

        public void gameStart(GameWebSocket mysocket, String enemyLogin) throws GameException {
            GameWebSocket enemysocket = gamePool.getFreeUserByLogin(enemyLogin);
            if(enemysocket != null)
                gamePool.startGame(mysocket, enemysocket);
            else
                throw new GameException("User not found or not free.");
        }

        public void gameGetFree() {
            JSONObject res = OutputJSONMessagesCreator.getMessageFreeUsers(gamePool);
            sendMessage(res);
        }

        public void gameError(GameException ex) {
            LOGGER.debug(ex.getMessage());
            JSONObject errJSON = new JSONObject();
            errJSON.put("action", "error");
            JSONObject data = new JSONObject();
            data.put("message", ex.getMessage());
            errJSON.put("data", data);
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
        if(gameSession != null && gameSession.getStarted()) {
            gamePool.stopGame(this, enemySocket);
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

    public String getMyLogin() {return myLogin;}

    public void setMyLogin(String login) { myLogin = login; }

    public void setSession(Session session) {this.session = session;}

    public Session getSession() {return session;}

    public void sendMessage(JSONObject output) {
        sendMessageWithSession(session, output);
    }

}
