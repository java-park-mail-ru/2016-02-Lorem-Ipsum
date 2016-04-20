package game.gameinternal.instance;

import org.json.JSONObject;

/**
 * Created by Installed on 20.04.2016.
 */
public class InvocationConvention {
    public static final String INPUT_TYPE = JSONObject.class.getName();
    public static final String OUTPUT_TYPE = JSONObject.class.getName();
    public static final String FUNCTION_NAME_PARAMETER = "function";
    public static final String ARGS_NAME_PARAMETER = "args";
    public static final String CHECKER_NAME_PARAMETER = "res";
}
