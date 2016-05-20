package game.gamemanagement.gametask;

import game.gameprimitives.physicalinstance.IGetStateProcessor;
import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;
import game.gameprimitives.physicalinstance.IScoreProcessor;
import game.gameprimitives.physicalinstance.IStopProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public class TaskFactory implements ITaskFactory {

    IPhysicalInstanceProcessor physicalInstanceProcessor;
    IScoreProcessor scoreProcessor;
    IGetStateProcessor stateProcessor;
    IStopProcessor stopProcessor;

    public TaskFactory (
            IPhysicalInstanceProcessor instanceProcessor,
            IScoreProcessor scoreProcessor,
            IGetStateProcessor getStateProcessor,
            IStopProcessor stopProcessor
    ) {
        this.physicalInstanceProcessor = instanceProcessor;
        this.scoreProcessor = scoreProcessor;
        this.stateProcessor = getStateProcessor;
        this.stopProcessor = stopProcessor;
    }

    @Override
    public GameMechTaskRedirrectPlatform getRedirrectPlatformTask(
            IPhysicalInstanceProcessor.NUM_PLAYER numPlayer,
            double vx
    ) {
        return new GameMechTaskRedirrectPlatform(
                physicalInstanceProcessor,
                numPlayer,
                vx
        );
    }

    @Override
    public GameMechTaskState getGetStateTask() {
        return new GameMechTaskState(
                physicalInstanceProcessor,
                stateProcessor
        );
    }

    @Override
    public GameMechTaskStep getStepTask() {
        return new GameMechTaskStep(
                physicalInstanceProcessor,
                scoreProcessor,
                stopProcessor,
                stateProcessor
        );
    }

}
