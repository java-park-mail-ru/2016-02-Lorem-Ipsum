package game.gameinternalold.instance;

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

    final ScriptEngine engine;

    public InstanceOperator(ScriptEngine engine) {
        this.engine = engine;
    }

    /*public void performSet(JSONObject entry) {
        try {
            Invocable invoker = (Invocable) engine;
            Object res = entry.has("args") ?
                    invoker.invokeFunction(entry.getString("function"), entry.getJSONObject("args").toString()) :
                    invoker.invokeFunction(entry.getString("function"), new JSONObject().toString());
            LOGGER.debug("Res of performSet : {}", (String) res);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
    }*/

    public JSONObject performGet(JSONObject entry) {
        //noinspection OverlyBroadCatchBlock
        try {
            Invocable invoker = (Invocable) engine;
            Object res = entry.has(InvocationConvention.ARGS_NAME_PARAMETER) ?
                    invoker.invokeFunction(entry.getString(InvocationConvention.FUNCTION_NAME_PARAMETER),
                            entry.getJSONObject(InvocationConvention.ARGS_NAME_PARAMETER).toString()) :
                    invoker.invokeFunction(entry.getString(InvocationConvention.FUNCTION_NAME_PARAMETER),
                            new JSONObject().toString());
            LOGGER.debug("Res of performGet : {}", (String) res);
            return new JSONObject((String)res);
        }
        catch (Exception ex) {
            LOGGER.debug(ex.getMessage());
        }
        return null;
    }
}
