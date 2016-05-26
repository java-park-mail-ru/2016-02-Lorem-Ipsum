package game.gameprimitives;

import org.json.JSONException;
import org.json.JSONObject;

public class Circle extends Body {
    private double radius;
    @Override
    public double top(){
        return this.yPos - this.radius;//Ось 0Y канваса возрастает сверху вниз
    }

    @Override
    public double bottom() {
        return this.yPos + this.radius;//Ось 0Y канваса возрастает сверху вниз
    }

    @Override
    public double right() {
        return this.xPos + this.radius;
    }

    @Override
    public double left() {
        return this.xPos - this.radius;
    }

    @SuppressWarnings("SameParameterValue")
    public Circle(double x, double y, double vx, double vy, double radius){
        super(x,y,vx,vy);
        this.radius = radius;
    }

    public Circle(JSONObject circle){
        fromJSON(circle);
    }

    public void  setRadius(double radius) {
        this.radius = radius;
    }

    public double getRadius() {
        return  radius;
    }

    @Override
    public void fromJSON(JSONObject circle) {
        super.fromJSON(circle);
        try {
            radius = circle.has("radius")?circle.getDouble("radius"):0;
        }
        catch (JSONException err) {
            LOGGER.debug(err.getMessage());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject circle = super.toJSON();
        circle.put("radius", radius);
        return circle;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Body clone() {
        return new Circle(this.toJSON());
    }

}
