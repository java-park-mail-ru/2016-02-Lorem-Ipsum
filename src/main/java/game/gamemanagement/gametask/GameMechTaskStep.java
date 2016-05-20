package game.gamemanagement.gametask;

import game.gameprimitives.physicalinstance.IGetStateProcessor;
import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;
import game.gameprimitives.physicalinstance.IScoreProcessor;
import game.gameprimitives.physicalinstance.IStopProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public class GameMechTaskStep implements IGameMechTask {

    IPhysicalInstanceProcessor physicalInstanceProcessor;
    IScoreProcessor scoreProcessor;
    IStopProcessor stopProcessor;
    IGetStateProcessor getStateProcessor;

    public GameMechTaskStep(
            IPhysicalInstanceProcessor instanceProcessor,
            IScoreProcessor scoreProcessor,
            IStopProcessor stopProcessor,
            IGetStateProcessor getStateProcessor
    ) {
        this.physicalInstanceProcessor = instanceProcessor;
        this.scoreProcessor = scoreProcessor;
        this.stopProcessor = stopProcessor;
        this.getStateProcessor = getStateProcessor;
    }

    @Override
    public void perform() {
        physicalInstanceProcessor.performGameStep();
        physicalInstanceProcessor.processCollisions(scoreProcessor);
        physicalInstanceProcessor.getState(getStateProcessor);
        physicalInstanceProcessor.processStop(stopProcessor);
    }
}
