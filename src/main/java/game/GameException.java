package game;

import game.gamemanagement.websocket.GameWebSocket;

/**
 * Created by Installed on 20.04.2016.
 */
public class GameException extends Exception {

    private final GameWebSocket socket;

    public GameException(GameWebSocket socket, String message) {
        super(message);
        this.socket = socket;
    }

    public GameWebSocket getSocket() {return this.socket;}

}
