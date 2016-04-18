package game.gameinternal.instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.StringWriter;

/**
 * Created by Installed on 17.04.2016.
 */
public class InstanceRunner implements Runnable {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    ScriptEngine engine;
    JSONObject entry;
    Thread thread;
    String path;

    public InstanceRunner(String name, String pathToScript, String pathToOutput, ScriptEngine engine, JSONObject entry) {
        thread = new Thread(this);
        this.path = pathToScript;
        this.engine = engine;
        this.entry = entry;
        try {
            ScriptContext context = engine.getContext();
            context.setWriter(new FileWriter(pathToOutput));
            engine.eval(new FileReader(pathToScript));
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
            return;
        }

        thread.start();
    }

    public void run() {
        try {
            Invocable invoceFunc = (Invocable) engine;
            invoceFunc.invokeFunction(entry.getString("function"), entry.getJSONObject("args").toString());
        }
        catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public Thread getThread() {
        return thread;
    }

}
