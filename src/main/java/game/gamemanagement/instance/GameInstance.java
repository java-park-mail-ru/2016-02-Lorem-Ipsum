package game.gamemanagement.instance;

import game.gamemanagement.IGameSettings;
import game.gamemanagement.gametask.IGameMechTask;
import game.gamemanagement.gametask.ITaskFactory;
import game.gamemanagement.gametask.TaskFactory;
import game.gameprimitives.physicalinstance.*;

/**
 * Created by Installed on 20.05.2016.
 */
public class GameInstance implements IGameInstance {

    PhysicalInstance physicalInstance;
    GameRunner gameRunner;
    GameOperator gameOperator;
    ITaskFactory taskFactory;

    public GameInstance(
            IScoreProcessor scoreProcessor,
            IGetStateProcessor getStateProcessor,
            IStopProcessor stopProcessor
    ) {
        physicalInstance = new PhysicalInstance(
                IGameSettings.CANVAS_WIDTH,
                IGameSettings.CANVAS_HEIGHT
        );
        taskFactory = new TaskFactory(
                physicalInstance.getProcessor(),
                scoreProcessor,
                getStateProcessor,
                stopProcessor
        );
        gameRunner = new GameRunner(taskFactory);
        gameOperator = new GameOperator();
    }

    @Override
    public void performGetState() {
        IGameMechTask task = taskFactory.getGetStateTask();
        gameOperator.pushTask(task);
    }

    @Override
    public void performRedirrectPlatform(
            IPhysicalInstanceProcessor.NUM_PLAYER numPlayer,
            double vx
    ) {
        IGameMechTask task = taskFactory.getRedirrectPlatformTask(numPlayer, vx);
        gameOperator.pushTask(task);
    }

    @Override
    public void performStop() {
        gameOperator.stop();
        gameRunner.stop();
    }

}
