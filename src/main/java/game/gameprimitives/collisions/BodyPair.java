package game.gameprimitives.collisions;

import game.gameprimitives.Body;
import game.gameprimitives.Canvas;
import game.gameprimitives.Circle;
import game.gameprimitives.Rectangle;

public class BodyPair {
    private Body first;
    private Body second;
    private CollisionHandler handler;

    public BodyPair(Body first, Body second, CollisionHandler handler){

        if(second instanceof Circle
                || ( !(first instanceof Circle)
                && ( second instanceof Rectangle) ) ) {
            Body tmp = second;
            second = first;
            first = tmp;
        }

        this.first = first;
        this.second = second;
        this.handler = handler;
    }

    public Body getFirst() {return first;}
    public Body getSecond() {return second;}

    private boolean checkCollisionVertical(){
        boolean result = false;
        if( !(first instanceof Canvas)
                && !(second instanceof Canvas)) {
            if ((first.top() <= second.bottom()) &&
                    (first.top() >= second.top())) {
                result = true;
            }
            if ((first.bottom() >= second.top()) &&
                    (first.top() <= second.top())) {
                result = true;
            }
        }
        else {
            if(first instanceof Canvas) {
                Body tmp = second; second = first; first = tmp;
            }
            Canvas canvas = (Canvas) second;
            if(first.bottom() >= canvas.getHeight()
                    || first.top() <= 0) {
                result = true;
            }
        }
        return result;
    }


    private boolean checkCollisionHorizontal(){
        boolean result = false;
        if( !(first instanceof Canvas)
                && !(second instanceof Canvas)) {
            if ((first.left() <= second.right()) &&
                    (first.left() >= second.left())) {
                result = true;
            }
            if ((first.right() >= second.left()) &&
                    (first.right() <= second.right())) {
                result = true;
            }
        }
        else {
            if(first instanceof Canvas) {
                Body tmp = second; second = first; first = tmp;
            }
            Canvas canvas = (Canvas) second;
            if(first.right() >= canvas.getWidth()
                    || first.left() <= 0) {
                result = true;
            }
        }
        return  result;
    }

    public boolean checkCollision(){
        return this.checkCollisionHorizontal() &&
                this.checkCollisionVertical();
    }

    public boolean onCollision() {
        boolean wasCollision;
        if(wasCollision = checkCollision()) {
            handler.handleCollision(first, second);
        }
        return wasCollision;
    }

}
