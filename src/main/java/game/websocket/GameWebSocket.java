package game.websocket;

import game.MessageConvention;
import game.gameinternalold.GameException;
import game.gameinternalold.GamePool;
import game.gameinternalold.GameSession;
import game.gameinternalold.instance.Stopable;
import game.gameprimitives.physicalinstance.PhysicalInstance;
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
    private final Handlers handlers;

    public GameWebSocket(Long myId, GamePool gamePool) {
        this.myId = myId;
        this.gamePool = gamePool;
        this.gameSession = null;
        this.enemySocket = null;
        this.handlers = new Handlers();
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
            String type;
            if (input.has(MessageConvention.InputMessageConvention.PARAMETER_NAME_INPUT_TYPE)) {
                type = input.getString(MessageConvention.InputMessageConvention.PARAMETER_NAME_INPUT_TYPE);
            }
            else
                throw new GameException("Type parameter missing.");

            switch (type) {
                case MessageConvention.InputMessageConvention.PARAMETER_NAME_TYPE_GAME_ACTION :
                    handlers.gameAction(input);
                    break;
                case MessageConvention.InputMessageConvention.PARAMETER_NAME_TYPE_GAME_START :
                    handlers.gameStart(input);
                    break;
                case MessageConvention.InputMessageConvention.PARAMETER_NAME_TYPE_GAME_INFO :
                    handlers.gameInfo();
                    break;
                default:
                    throw new GameException("Unknown action.");
            }
            /*if (type.equals("gameStop")) {
                stop();
                return;
            }*/
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
                JSONObject output = gameSession.performAction(myId, input);
                sendMessage(output);
                if (output.has(MessageConvention.OutputMessageConvention.PARAMETER_NAME_SEND_TO_ENEMY)) {
                    if (output.getBoolean(MessageConvention.OutputMessageConvention.PARAMETER_NAME_SEND_TO_ENEMY)) {
                        enemySocket.sendMessage(output);
                    }
                }
            }
            else {
                throw new GameException("Unable to perform action, improper condition.");
            }
        }

        public void gameStart(JSONObject input) throws GameException {
            if(input.has(MessageConvention.InputMessageConvention.PARAMETER_NAME_ENEMY_ID)) {
                Long enemyId = input.getLong(MessageConvention.InputMessageConvention.PARAMETER_NAME_ENEMY_ID);
                gamePool.startGame(myId, enemyId);
            }
            else
                throw new GameException("Enemy id not specified");
        }

        public void gameInfo() {
            JSONArray arr = gamePool.getFreeUsersArray();
            JSONObject res = new JSONObject();
            res.put(MessageConvention.OutputMessageConvention.PARAMETER_NAME_GAME_INFO, arr);
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
}
