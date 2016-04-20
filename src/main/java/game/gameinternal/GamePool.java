package game.gameinternal;

import database.DbService;
import database.IDbService;
import game.websocket.GameWebSocket;
import main.IGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Installed on 17.04.2016.
 */
public class GamePool {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    //private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private Map<Long, GameWebSocket> connectedUsers = new ConcurrentHashMap<>();
    private Set<Long> freeUsers = new ConcurrentHashSet<>();
    private Map<Long, GameSession> games = new ConcurrentHashMap<>();
    private IGame dbService;
    private static String pathToMechanic;
    private static String pathToOutput;

    public GamePool(String _pathToMechanic, String _pathToOutput, IGame dbService) {
        pathToMechanic = _pathToMechanic;
        pathToOutput = _pathToOutput;
        this.dbService = dbService;
    }

    public void connectUser(Long userId, GameWebSocket gameWebSocket) throws GameException {
        if(userId != null && gameWebSocket != null) {
            connectedUsers.put(userId, gameWebSocket);
            freeUsers.add(userId);
            gameWebSocket.setGamePool(this);
        }
        else {
            throw new GameException("Cannot connect user, improper condition.");
        }
    }

    public void disconnectUser(Long userId) throws GameException {
        if(userId != null) {
            if(connectedUsers.containsKey(userId))
                connectedUsers.remove(userId);

            if(freeUsers.contains(userId))
                freeUsers.remove(userId);

            if(games.containsKey(userId)) {
                games.get(userId).getFirstWebSocket().stop();
            }
        }
        else {
            throw new GameException("Cannot disconnect user, improper condition.");
        }
    }

    public void stopGame(Long userIdFirst, Long userIdSecond) throws GameException {
        Long starterId = null;
        if(games.containsKey(userIdFirst))
            starterId = userIdFirst;
        else if(games.containsKey(userIdSecond))
            starterId = userIdSecond;

        if(starterId != null) {
            GameSession gameSession = games.get(starterId);
            games.remove(starterId);
            freeUsers.add(userIdFirst);
            freeUsers.add(userIdSecond);
            gameSession.stop(new JSONObject(), new JSONObject(), dbService);
        }

    }

    public void startGame(Long userIdStarter, Long userIdSecond) throws GameException {
        JSONObject entryFirst = new JSONObject();
        JSONObject entrySecond = new JSONObject();
        JSONObject entryArgsFirst = new JSONObject();
        JSONObject entryArgsSecond = new JSONObject();
        JSONObject entryMessageFirst = new JSONObject();
        JSONObject entryMessageSecond = new JSONObject();
        JSONObject entryToStop = new JSONObject();
        if(connectedUsers.containsKey(userIdStarter) && connectedUsers.containsKey(userIdSecond)
                && freeUsers.contains(userIdStarter) && freeUsers.contains(userIdSecond)
                && !userIdStarter.equals(userIdSecond)) {

            entryFirst.put("function", "start");
            entryArgsFirst.put("myId", userIdStarter);
            entryArgsFirst.put("enemyId", userIdSecond);
            entryFirst.put("args", entryArgsFirst);

            entrySecond.put("function", "start");
            entryArgsSecond.put("myId", userIdSecond);
            entryArgsSecond.put("enemyId", userIdStarter);
            entrySecond.put("args", entryArgsSecond);

            entryToStop.put("function", "check");
            entryToStop.put("args", new JSONObject());

            entryMessageFirst.put("type", "startGame");
            entryMessageFirst.put("id", userIdStarter);
            entryMessageFirst.put("enemyId", userIdSecond);
            entryMessageSecond.put("type", "startGame");
            entryMessageSecond.put("id", userIdSecond);
            entryMessageSecond.put("enemyId", userIdStarter);

            GameSession gameSession = new GameSession();
            GameWebSocket webSocketStarter = connectedUsers.get(userIdStarter);
            GameWebSocket webSocketSecond = connectedUsers.get(userIdSecond);
            gameSession.init(userIdStarter, userIdSecond, webSocketStarter, webSocketSecond);
            gameSession.start(pathToMechanic, pathToOutput, entryFirst,
                    entrySecond, entryMessageFirst, entryMessageSecond, entryToStop);
            freeUsers.remove(userIdStarter);
            freeUsers.remove(userIdSecond);
            games.put(userIdStarter, gameSession);

        }
        else {
            throw new GameException("Unable to start game, improper condition.");
        }
    }

    public JSONArray getFreeUsersArray() {
        JSONArray res = new JSONArray();
        for( Long id : freeUsers ) {
            res.put(id);
        }
        return res;
    }



}
