package game.gamemanagement.instance;

import game.Stopable;
import game.gamemanagement.IGameThreadSettings;
import game.gamemanagement.gametask.IGameMechTask;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Created by Installed on 20.05.2016.
 */
public class GameOperator implements Runnable, Stopable {

    final Queue<IGameMechTask> tasks = new ArrayDeque<>();
    final Thread thread;

    public GameOperator() {
        thread = new Thread(this);
        thread.start();
    }

    public void pushTask(IGameMechTask task) {
        tasks.add(task);
    }

    @Override
    public void stop() {
        thread.interrupt();
    }

    @Override
    public void run() {
        while (!thread.isInterrupted()) {
            if(!tasks.isEmpty()) {
                IGameMechTask task = tasks.poll();
                task.perform();
            }
            try {
                Thread.sleep(IGameThreadSettings.GAME_RUNNER_SLEEP);
            }
            catch (InterruptedException e) {

            }
        }
    }
}
