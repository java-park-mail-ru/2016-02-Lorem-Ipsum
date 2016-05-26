package game.gameprimitives.collisions;

import game.gameprimitives.*;

/**
 * Created by Installed on 09.05.2016.
 */
public class GlobalCollisionHandler implements CollisionHandler {

    @Override
    public void handleCollision(Body first, Body second) {

        if (first instanceof Rectangle) {
            Canvas canvas = (Canvas) second;
            handleCollisionBoundPlatform((Rectangle)first, canvas.getWidth(), canvas.getHeight());
        }
        else if (first instanceof Circle) {
            if(second instanceof Blocks)
                handleCollisionBlocksBall((Blocks) second, (Circle) first);
            if(second instanceof Canvas) {
                Canvas canvas = (Canvas) second;
                handleCollisionBoundBall((Circle) first, canvas.getWidth(), canvas.getHeight());
            }
            if(second instanceof Rectangle)
                handleCollisionPlatformBall((Rectangle) second, (Circle) first);
        }

    }

    public static void handleCollisionPlatformBall(Rectangle platform, Circle ball) {
         ball.setVY(ball.getVY() * (-1));
         double platformCenter = platform.getX() + platform.getWidth() / 2;
         ball.setVX( 3 * (ball.getX() - platformCenter) / platform.getWidth() );
    }

    public static void handleCollisionBlocksBall(Blocks blocks, Circle ball) {
        int row = (int)( ball.top() / ( blocks.getBlockHeight() + blocks.getPaddingY() ) );
        int col = (int)( ball.getX() / ( blocks.getBlockWidth() + blocks.getPaddingX() ) );
        if(blocks.attackBlock(row, col)) {
            ball.setVY(ball.getVY() * (-1));
        }
    }

    public static void handleCollisionBoundBall(Circle ball, double canvasWidth, double canvasHeight) {
        if(ball.top() <= 0 || ball.bottom() >= canvasHeight)
            ball.setVY(ball.getVY() * (-1));
        if(ball.left() <= 0 || ball.right() >= canvasWidth)
            ball.setVX(ball.getVX() * (-1));
    }

    @SuppressWarnings("UnusedParameters")
    public static void handleCollisionBoundPlatform(Rectangle platform, double canvasWidth, double canvasHeight) {
        if(platform.getX() <= 0)
            platform.setX(0);
        if(platform.getX() + platform.getWidth() >= canvasWidth)
            platform.setX(canvasWidth - platform.getWidth());
    }

}
