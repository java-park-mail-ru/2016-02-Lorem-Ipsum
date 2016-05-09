package game.gameinternalold.instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Installed on 19.04.2016.
 */
public class InstanceChecker implements Runnable {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private static final int SLEEP_PERIOD = 10000;
    private final InstanceOperator instanceOperator;
    final JSONObject entry;
    final Thread thread;
    final Stopable toStop;

    public InstanceChecker(String name, InstanceOperator instanceOperator, JSONObject entry, Stopable toStop) {
        thread = new Thread(this, name);
        this.instanceOperator = instanceOperator;
        this.entry = entry;
        this.toStop = toStop;
        thread.start();
    }

    @Override
    public void run() {
        //noinspection OverlyBroadCatchBlock
        try {
            JSONObject res = instanceOperator.performGet(entry);
            while (!res.getBoolean(InvocationConvention.CHECKER_NAME_PARAMETER) && !thread.isInterrupted()) {
                res = instanceOperator.performGet(entry);
                Thread.sleep(SLEEP_PERIOD);
                LOGGER.debug("Res of check " + String.valueOf(res) + ' ' + String.valueOf(new Date(System.currentTimeMillis())));
            }
            toStop.stop();
        }
        catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public Thread getThread() {return thread;}

    public void stop() { thread.interrupt(); }
}
