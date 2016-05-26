package game.gamemanagement.instance;

import game.Stopable;
import game.gamemanagement.IGameThreadSettings;
import game.gamemanagement.gametask.IGameMechTask;
import game.gamemanagement.gametask.ITaskFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by Installed on 20.05.2016.
 */
@SuppressWarnings("CanBeFinal")
public class GameRunner implements Runnable, Stopable {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    IGameMechTask task;
    final Thread thread;

    public GameRunner(
            ITaskFactory taskFactory
    ) {
        task = taskFactory.getStepTask();
        thread = new Thread(this);
        thread.start();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            task.perform();
            try {
                Thread.sleep(IGameThreadSettings.GAME_RUNNER_SLEEP);
            }
            catch (InterruptedException e) {
                LOGGER.debug(e.getMessage());
            }
        }
    }

    @Override
    public void stop() {
        thread.interrupt();
    }

}
