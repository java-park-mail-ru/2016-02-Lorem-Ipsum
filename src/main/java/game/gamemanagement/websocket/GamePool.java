package game.gamemanagement.websocket;

import game.GameException;
import game.gamemanagement.gamemessages.OutputMessageListFree;
import main.IGame;
import messagesystem.Address;
import messagesystem.MessageSystem;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Installed on 17.04.2016.
 */
@SuppressWarnings({"FieldCanBeLocal", "ConstantConditions"})
public class GamePool {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private final Map<Long, GameWebSocket> connectedUsers = new ConcurrentHashMap<>();
    private final Map<String, GameWebSocket> freeUsers = new ConcurrentHashMap<>();
    private final Map<Long, GameSession> games = new ConcurrentHashMap<>();
    private final IGame dbService;

    public GamePool(IGame dbService) {
        this.dbService = dbService;
    }

    public void connectUser(GameWebSocket gameWebSocket) throws GameException {
        Long userId = null;
        if( gameWebSocket != null
                && (userId = gameWebSocket.getMyId()) != null)
        {
            connectedUsers.put(userId, gameWebSocket);
            freeUsers.put(gameWebSocket.getMyLogin(), gameWebSocket);
            gameWebSocket.setGamePool(this);
        }
        else {
            throw new GameException(gameWebSocket, "Cannot connect user. Websocket is null: " + (gameWebSocket == null)
                                    + "Userid is null: " + (userId == null) );
        }
    }

    public void disconnectUser(GameWebSocket webSocket) throws GameException {
        if(webSocket != null) {
            Long userId = webSocket.getMyId();
            String login = webSocket.getMyLogin();

            if(connectedUsers.containsKey(userId))
                connectedUsers.remove(userId);

            if(freeUsers.containsKey(login))
                freeUsers.remove(login);

            if(games.containsKey(userId)) {
                games.get(userId).stop(webSocket);
                games.remove(userId);
            }
        }
        else {
            throw new GameException(webSocket, "Cannot disconnect user. Websocket is null: " + (webSocket == null));
        }
    }

    public void stopGame(GameWebSocket firstSocket, GameWebSocket secondSocket) throws GameException {
        if(firstSocket != null
                && secondSocket != null) {

            Long userIdFirst = firstSocket.getMyId();
            Long userIdSecond = secondSocket.getMyId();

            Long starterId = null;
            if (games.containsKey(userIdFirst))
                starterId = userIdFirst;
            else if (games.containsKey(userIdSecond))
                starterId = userIdSecond;

            if (starterId != null) {
                GameSession gameSession = games.get(starterId);
                games.remove(starterId);
                freeUsers.put(firstSocket.getMyLogin(), firstSocket);
                freeUsers.put(secondSocket.getMyLogin(), secondSocket);
                gameSession.stop(firstSocket);
            }
        }

    }

    public void startGame(GameWebSocket firstSocket, GameWebSocket secondSocket) throws GameException {
        if(firstSocket != null
                && secondSocket != null) {

            Long userIdStarter = firstSocket.getMyId();
            Long userIdSecond = secondSocket.getMyId();

            //noinspection OverlyComplexBooleanExpression
            if (connectedUsers.containsKey(userIdStarter)
                    && connectedUsers.containsKey(userIdSecond)
                    && freeUsers.containsKey(firstSocket.getMyLogin())
                    && freeUsers.containsKey(secondSocket.getMyLogin())
                    && !userIdStarter.equals(userIdSecond)) {


                GameSession gameSession = new GameSession(
                        this,
                        firstSocket,
                        secondSocket
                );
                gameSession.start(firstSocket);
                freeUsers.remove(firstSocket.getMyLogin());
                freeUsers.remove(secondSocket.getMyLogin());
                games.put(userIdStarter, gameSession);
            } else {
                throw new GameException(firstSocket,
                        "Unable to start game. First user is connected: "+ connectedUsers.containsKey(userIdStarter)
                        + " Second user is connetcted: " + connectedUsers.containsKey(userIdSecond) +
                        " First user is free: " + freeUsers.containsKey(firstSocket.getMyLogin()) +
                        " Second user is free:" + freeUsers.containsKey(secondSocket.getMyLogin()) +
                        " Different users: " + !userIdStarter.equals(userIdSecond));
            }
        }
    }

    public JSONArray getFreeUsersArray() {
        JSONArray res = new JSONArray();
        freeUsers.keySet().forEach(res::put);
        return res;
    }

    public GameWebSocket getFreeUserByLogin(String login) {
        if(freeUsers.containsKey(login))
            return freeUsers.get(login);
        else
            return null;
    }

    //new
    public void notifyAllFreeUsers() {
        for(String login : freeUsers.keySet()) {
            GameWebSocket socket = freeUsers.get(login);
            Address address = MessageSystem.getInstance().getAddressService().getOutputProcessorAddress();
            MessageSystem.getInstance().sendMessage(
                    new OutputMessageListFree(
                            address,
                            address,
                            socket
                    )
            );
        }
    }

}
