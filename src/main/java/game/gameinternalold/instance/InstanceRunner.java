package game.gameinternalold.instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by Installed on 17.04.2016.
 */
public class InstanceRunner implements Runnable {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    final ScriptEngine engine;
    final JSONObject entry;
    final Thread thread;
    final String path;

    public InstanceRunner(String name, String pathToScript, String pathToOutput, ScriptEngine engine, JSONObject entry) {
        thread = new Thread(this, name);
        this.path = pathToScript;
        this.engine = engine;
        this.entry = entry;
        //noinspection OverlyBroadCatchBlock,OverlyBroadCatchBlock,OverlyBroadCatchBlock
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

    @Override
    public void run() {
        //noinspection OverlyBroadCatchBlock
        try {
            Invocable invoker = (Invocable) engine;
            @SuppressWarnings("UnusedAssignment") Object res = entry.has(InvocationConvention.ARGS_NAME_PARAMETER) ?
                    invoker.invokeFunction(entry.getString(InvocationConvention.FUNCTION_NAME_PARAMETER),
                            entry.getJSONObject(InvocationConvention.ARGS_NAME_PARAMETER).toString()) :
                    invoker.invokeFunction(entry.getString(InvocationConvention.FUNCTION_NAME_PARAMETER),
                            new JSONObject().toString());
        }
        catch (Exception e) {
            LOGGER.debug(e.getMessage());
        }
    }

    public Thread getThread() {
        return thread;
    }

}
