package game.gameinternal;

import database.DbService;
import database.IDbService;
import game.websocket.GameWebSocket;
import main.IGame;
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

    public void connectUser(Long userId, GameWebSocket gameWebSocket) {
        if(userId != null && gameWebSocket != null) {
            connectedUsers.put(userId, gameWebSocket);
            freeUsers.add(userId);
            gameWebSocket.setGamePool(this);
        }
    }

    public void disconnectUser(Long userId) {
        if(userId != null) {
            if(!connectedUsers.containsKey(userId))
                connectedUsers.remove(userId);
            freeUsers.add(userId);
        }
    }

    public void stopGame(Long userIdFirst, Long userIdSecond) {
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

    public void startGame(Long userIdStarter, Long userIdSecond) {
        JSONObject entryFirst = new JSONObject();
        JSONObject entrySecond = new JSONObject();
        if(connectedUsers.containsKey(userIdStarter) && connectedUsers.containsKey(userIdSecond)
                && freeUsers.contains(userIdStarter) && freeUsers.contains(userIdSecond)
                && !userIdStarter.equals(userIdSecond)) {
            entryFirst.put("function", "start");
            entryFirst.put("id", userIdStarter);
            entrySecond.put("function", "start");
            entrySecond.put("id", userIdSecond);
            GameSession gameSession = new GameSession();
            GameWebSocket webSocketStarter = connectedUsers.get(userIdStarter);
            GameWebSocket webSocketSecond = connectedUsers.get(userIdSecond);
            gameSession.init(userIdStarter, userIdSecond, webSocketStarter, webSocketSecond);
            gameSession.start(pathToMechanic, pathToOutput, entryFirst, entrySecond);
            freeUsers.remove(userIdStarter);
            freeUsers.remove(userIdSecond);
            games.put(userIdStarter, gameSession);

        }
    }

    public JSONArray getConnectedUsers() {
        JSONArray res = new JSONArray();
        for( Long id : connectedUsers.keySet() ) {
            res.put(id);
        }
        return res;
    }

}
