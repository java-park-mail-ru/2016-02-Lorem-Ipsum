package game.gameinternal.instance;

import org.json.JSONObject;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

/**
 * Created by Installed on 17.04.2016.
 */
@SuppressWarnings("PublicField")
public class Instance {

    @SuppressWarnings("FieldCanBeLocal")
    private final ScriptEngine scriptEngine;
    private final String name;
    public final InstanceRunner instanceRunner;
    public final InstanceOperator instanceOperator;
    public InstanceChecker instanceChecker;

    public Instance(String name, String path, String pathToOutput, JSONObject entry) {
        this.name = name;
        scriptEngine = new ScriptEngineManager().getEngineByName("nashorn");
        instanceOperator = new InstanceOperator(scriptEngine);
        instanceRunner = new InstanceRunner(name, path, pathToOutput, scriptEngine, entry);
        instanceChecker = null;
    }

    @SuppressWarnings("deprecation")
    public void setInstanceChecker(JSONObject entry, Stopable stopable) {
        if(instanceChecker != null && instanceChecker.getThread().isAlive())
            instanceChecker.getThread().stop();
        instanceChecker = new InstanceChecker( this.name + "checker",instanceOperator, entry, stopable);
    }

    @SuppressWarnings("deprecation")
    public void close() {
        instanceRunner.getThread().stop();
        if(instanceChecker != null) {
            Thread instanceCheckerthread = instanceChecker.getThread();
            if(!instanceCheckerthread.equals(Thread.currentThread()) && instanceCheckerthread.isAlive()) {
                instanceChecker.stop();
            }
        }
    }
}
