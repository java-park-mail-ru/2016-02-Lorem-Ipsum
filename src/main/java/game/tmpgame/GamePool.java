package game.tmpgame;

import game.MessageConvention;
import game.gameinternalold.GameException;
import main.IGame;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.util.ConcurrentHashSet;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Installed on 17.04.2016.
 */
public class GamePool {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    //private static final AtomicInteger ID_GENERATOR = new AtomicInteger(0);
    private final Map<Long, GameWebSocket> connectedUsers = new ConcurrentHashMap<>();
    private final Set<Long> freeUsers = new ConcurrentHashSet<>();
    private final Map<Long, GameSession> games = new ConcurrentHashMap<>();
    private final IGame dbService;

    public GamePool(IGame dbService) {
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
                games.remove(userId);
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
            gameSession.stop();
        }

    }

    public void startGame(Long userIdStarter, Long userIdSecond) throws GameException {
        //noinspection OverlyComplexBooleanExpression
        if(connectedUsers.containsKey(userIdStarter)
                && connectedUsers.containsKey(userIdSecond)
                && freeUsers.contains(userIdStarter)
                && freeUsers.contains(userIdSecond)
                && !userIdStarter.equals(userIdSecond)) {


            GameSession gameSession = new GameSession();
            GameWebSocket webSocketStarter = connectedUsers.get(userIdStarter);
            GameWebSocket webSocketSecond = connectedUsers.get(userIdSecond);
            gameSession.init(userIdStarter, userIdSecond, webSocketStarter, webSocketSecond);
            gameSession.start();
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

    public Long getSomeFreePlayer(Long searcherId) {
        if(!freeUsers.isEmpty()) {
            Object[] freeUsersArr = freeUsers.toArray();
            for(int i = 0; i < freeUsersArr.length; i++) {
                if((Long)freeUsersArr[i] != searcherId) {
                    return (Long)freeUsersArr[i];
                }
            }
            return -1L;
        }
        else
            return -1L;
    }

}
