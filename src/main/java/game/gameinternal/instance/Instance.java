package game.gameinternal.instance;

import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Installed on 17.04.2016.
 */
public class Instance {

    private ScriptEngine scriptEngine;
    private String name;
    public InstanceRunner instanceRunner;
    public InstanceOperator instanceOperator;

    public Instance(String name, String path, String pathToOutput, JSONObject entry) {
        this.name = name;
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        instanceOperator = new InstanceOperator(scriptEngine);
        instanceRunner = new InstanceRunner(name, path, pathToOutput, scriptEngine, entry);
    }

    public void close() {
        instanceRunner.getThread().stop();
    }
}
