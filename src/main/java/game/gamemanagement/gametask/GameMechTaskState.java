package game.gamemanagement.gametask;

import game.gameprimitives.physicalinstance.IGetStateProcessor;
import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public class GameMechTaskState implements IGameMechTask {

    IPhysicalInstanceProcessor physicalInstanceProcessor;
    IGetStateProcessor getStateProcessor;

    public GameMechTaskState(
            IPhysicalInstanceProcessor instanceProcessor,
            IGetStateProcessor getStateProcessor
    ) {
        this.physicalInstanceProcessor = instanceProcessor;
        this.getStateProcessor = getStateProcessor;
    }

    @Override
    public void perform() {
        physicalInstanceProcessor.getState(getStateProcessor);
    }
}
