package game.gameprimitives.physicalinstance;

/**
 * Created by Installed on 10.05.2016.
 */
public interface IPhysicalInstanceProcessor {
    @SuppressWarnings("EnumeratedClassNamingConvention")
    enum COLLISION_TYPE {BALL_PLATFORM, BALL_BLOCKS, BALL_BOUND, PLATFORM_BOUND, UNKNOWN}

    @SuppressWarnings("EnumeratedClassNamingConvention")
    enum NUM_PLAYER {FIRST, SECOND}

    void processCollisions(IScoreProcessor scoreProcessor);
    void processStop(IStopProcessor stopProcessor);
    void performGameStep();
    void redirrectPlatform(NUM_PLAYER numPlayer, double vx);
    void getState(IGetStateProcessor processor);
}
