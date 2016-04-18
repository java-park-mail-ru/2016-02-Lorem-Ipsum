package game.gameinternal.instance;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;

import javax.script.Invocable;
import javax.script.ScriptEngine;

/**
 * Created by Installed on 17.04.2016.
 */
public class InstanceOperator {

    public static final Logger LOGGER = LogManager.getLogger("GameLogger");

    ScriptEngine engine;

    public InstanceOperator(ScriptEngine engine) {
        this.engine = engine;
    }

    public void performSet(JSONObject entry) {
        try {
            Invocable invoker = (Invocable) engine;
            invoker.invokeFunction(entry.getString("function"), entry.getJSONObject("args").toString());
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
    }

    public JSONObject performGet(JSONObject entry) {
        try {
            Invocable invoker = (Invocable) engine;
            Object res = invoker.invokeFunction(entry.getString("function"), entry.getJSONObject("args").toString());
            return new JSONObject((String)res);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
        return null;
    }
}
