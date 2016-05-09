package game.gameprimitives.physicalinstance;

import game.gameprimitives.collisions.BodyPair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Queue;

/**
 * Created by Installed on 10.05.2016.
 */
public interface IPhysicalInstanceProcessor {
    enum COLLISION_TYPE {BALL_PLATFORM, BALL_BLOCKS, BALL_BOUND, PLATFORM_BOUND, UNKNOWN};
    enum NUM_PLAYER {FIRST, SECOND};
    void processCollisions(IScoreProcessor scoreProcessor);
    void performGameStep();
    void redirrectPlatform(NUM_PLAYER numPlayer, double vx);
    JSONObject getState();
}
