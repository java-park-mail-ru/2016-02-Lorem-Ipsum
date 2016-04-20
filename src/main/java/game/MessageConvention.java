package game;

/**
 * Created by Installed on 20.04.2016.
 */
public class MessageConvention {
    public static class InputMessageConvention {
        public static final String PARAMETER_NAME_INPUT_TYPE = "type";
        public static final String PARAMETER_NAME_TYPE_GAME_INFO = "gameInfo";
        public static final String PARAMETER_NAME_TYPE_GAME_ACTION = "gameAction";
        public static final String PARAMETER_NAME_ENEMY_ID = "enemyId";
        public static final String PARAMETER_NAME_TYPE_GAME_START = "gameStart";
    }
    public static class OutputMessageConvention {
        public static final String PARAMETER_NAME_OUTPUT_TYPE = "type";
        public static final String PARAMETER_NAME_GAME_INFO = "users";
        public static final String PARAMETER_NAME_SEND_TO_ENEMY = "sendToEnemy";
        public static final String PARAMETER_NAME_WIN = "win";
        public static final String PARAMETER_NAME_TYPE_STOP_GAME = "stopGame";
        public static final String PARAMETER_NAME_TYPE_START_GAME = "startGame";
        public static final String SCORE_FUNCTION = "score";
        public static final String SCORE_PARAMETER = "score";
        public static final String START_FUNCTION = "start";
        public static final String CHECK_FUNCTION = "check";
    }
}
