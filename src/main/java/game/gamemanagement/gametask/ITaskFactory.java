package game.gamemanagement.gametask;

import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public interface ITaskFactory {
    GameMechTaskRedirrectPlatform getRedirrectPlatformTask(
            IPhysicalInstanceProcessor.NUM_PLAYER numPlayer,
            double vx
    );
    GameMechTaskState getGetStateTask();
    GameMechTaskStep getStepTask();
}
