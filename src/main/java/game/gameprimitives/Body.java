package game.gameprimitives;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

public abstract class Body implements Comparable<Body> {
    public static final Logger LOGGER = LogManager.getLogger("GameLogger");
    protected double xPos;//позиция по x
    protected double yPos;//позиция по y
    protected double xVel;//скорость по х
    protected double yVel;//скорость по у

    public double getX() {
        return xPos;
    }
    public double getY() {
        return yPos;
    }
    public double getVX() {
        return xVel;
    }
    public double getVY() {
        return yVel;
    }

    public void setX(double x) {
        this.xPos = x;
    }
    public void setY(double y) {
        this.yPos = y;
    }
    public void setVX(double vx) {
        this.xVel = vx;
    }
    public void setVY(double vy) {
        this.yVel = vy;
    }

    @Override
    public int compareTo(@NotNull Body  other){ //Для сортировки по левому краю
        return  (int)(this.left() - other.left());
    }

    public abstract double top();
    public abstract double bottom();
    public abstract double right();
    public abstract double left();

    Body(){
        xPos = yPos =xVel = yVel = 0;
    }
    Body(double x, double y, double vx, double vy){
        xPos = x;
        yPos = y;
        xVel = vx;
        yVel = vy;
    }
    Body(JSONObject body){
        fromJSON(body);
    }

    public void fromJSON(JSONObject body) {
        try {
            xPos = body.has("x") ? body.getDouble("x") : 0;
            yPos = body.has("y") ? body.getDouble("y") : 0;
            xVel = body.has("vx") ? body.getDouble("vx") : 0;
            yVel = body.has("vy") ? body.getDouble("vy") : 0;
        }
        catch(Exception err){
            LOGGER.debug(err.getMessage());
        }
    }

    public JSONObject toJSON() {
        JSONObject body = new JSONObject();
        body.put("x", xPos);
        body.put("y", yPos);
        body.put("vx", xVel);
        body.put("vy", yVel);
        return  body;
    }

    public abstract Body clone();

}
