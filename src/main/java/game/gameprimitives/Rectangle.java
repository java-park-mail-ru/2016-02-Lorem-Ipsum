package game.gameprimitives;

import org.json.JSONException;
import org.json.JSONObject;

public class Rectangle extends Body {
    //Прямоугольник задаётся верхней левой точкой
    private double width;
    private double height;

    public double getWidth() {
        return width;
    }
    public double getHeight() {
        return height;
    }

    public void setWidth(double width) {
        this.width = width;
    }
    public void setHeight(double height) {
        this.height = height;
    }

    @Override
    public double top(){
        return this.yPos;//Ось 0Y канваса возрастает сверху вниз
    }
    @Override
    public double bottom() {
        return this.yPos + this.height;//Ось 0Y канваса возрастает сверху вниз
    }
    @Override
    public double right() {
        return this.xPos + this.width;
    }
    @Override
    public double left() {
        return this.xPos;
    }

    public Rectangle(){
        this.width = 0;
        this.height = 0;
    }
    public Rectangle(double x, double y, double vx, double vy, double w, double h){
        super(x,y,vx,vy);
        width = w;
        height = h;
    }
    public Rectangle(JSONObject rect){
        fromJSON(rect);
    }

    @Override
    public void fromJSON(JSONObject rect) {
        super.fromJSON(rect);
        try {
            width = rect.has("width")?rect.getDouble("width"):0;
            height = rect.has("height")?rect.getDouble("height"):0;
        }
        catch (JSONException err) {
            LOGGER.debug(err.getMessage());
        }
    }

    @Override
    public JSONObject toJSON() {
        JSONObject rect = super.toJSON();
        rect.put("width", width);
        rect.put("height", height);
        return rect;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Body clone() {
        return new Rectangle(this.toJSON());
    }
}
