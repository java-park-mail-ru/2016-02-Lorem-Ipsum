package game.gamemanagement.gametask;

import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
@SuppressWarnings({"CanBeFinal", "InstanceVariableNamingConvention"})
public class GameMechTaskRedirrectPlatform implements IGameMechTask {

    IPhysicalInstanceProcessor physicalInstanceProcessor;
    IPhysicalInstanceProcessor.NUM_PLAYER numPlayer;
    double vx;

    public GameMechTaskRedirrectPlatform(
            IPhysicalInstanceProcessor instanceProcessor,
            IPhysicalInstanceProcessor.NUM_PLAYER numPlayer,
            double vx
    ) {
        this.physicalInstanceProcessor = instanceProcessor;
        this.numPlayer = numPlayer;
        this.vx = vx;
    }

    @Override
    public void perform() {
        physicalInstanceProcessor.redirrectPlatform(numPlayer, vx);
    }
}
