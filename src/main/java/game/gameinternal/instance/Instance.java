package game.gameinternal.instance;

import game.websocket.GameWebSocket;
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
    public InstanceChecker instanceChecker;

    public Instance(String name, String path, String pathToOutput, JSONObject entry) {
        this.name = name;
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        instanceOperator = new InstanceOperator(scriptEngine);
        instanceRunner = new InstanceRunner(name, path, pathToOutput, scriptEngine, entry);
        instanceChecker = null;
    }

    public void setInstanceChecker(JSONObject entry, Stopable stopable) {
        if(instanceChecker != null && instanceChecker.getThread().isAlive())
            instanceChecker.getThread().stop();
        instanceChecker = new InstanceChecker( this.name + "checker",instanceOperator, entry, stopable);
    }

    public void close() {
        instanceRunner.getThread().stop();
        if(instanceChecker.getThread().isAlive()) {
            instanceChecker.getThread().stop();
        }
    }
}
