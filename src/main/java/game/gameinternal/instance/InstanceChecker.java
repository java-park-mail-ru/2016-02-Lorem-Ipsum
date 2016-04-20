package game.gameinternal.instance;

import javafx.scene.paint.Stop;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.Date;

/**
 * Created by Installed on 19.04.2016.
 */
public class InstanceChecker implements Runnable {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    private static final int SLEEP_PERIOD = 10000;
    private InstanceOperator instanceOperator;
    JSONObject entry;
    Thread thread;
    Stopable toStop;

    public InstanceChecker(String name, InstanceOperator instanceOperator, JSONObject entry, Stopable toStop) {
        thread = new Thread(this, name);
        this.instanceOperator = instanceOperator;
        this.entry = entry;
        this.toStop = toStop;
        thread.start();
    }

    public void run() {
        try {
            JSONObject res = instanceOperator.performGet(entry);
            while (!res.getBoolean("res") && !thread.isInterrupted()) {
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
