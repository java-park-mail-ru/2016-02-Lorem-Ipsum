package game.gamemanagement.instance;

import game.gameprimitives.physicalinstance.IPhysicalInstanceProcessor;

/**
 * Created by Installed on 20.05.2016.
 */
public interface IGameInstance {
    void performGetState();
    void performRedirrectPlatform(
            IPhysicalInstanceProcessor.NUM_PLAYER numPlayer,
            double vx
    );
    void performStop();
}
