package game.gameprimitives.physicalinstance;

import game.gameprimitives.*;
import game.gameprimitives.collisions.BodyPair;
import game.gameprimitives.collisions.CollisionHandler;
import game.gameprimitives.collisions.GlobalCollisionHandler;
import game.gameprimitives.factories.IPrimitivesFactory;
import game.gameprimitives.factories.PrimitivesFactory;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Installed on 09.05.2016.
 */
@SuppressWarnings({"FieldCanBeLocal", "CanBeFinal"})
public class PhysicalInstance {

    private Blocks blocks;
    private Canvas canvas;
    private Circle ballFirst;
    private Circle ballSecond;
    private Rectangle platformFirst;
    private Rectangle platformSecond;
    private Set<BodyPair> collisionsFirst;
    private Set<BodyPair> collisionsSecond;
    private PhysicalInstanceProcessor physicalInstanceProcessor;


    @SuppressWarnings("SameParameterValue")
    public PhysicalInstance(double canvasWidth, double canvasHeight) {
        IPrimitivesFactory primitivesFactory = new PrimitivesFactory(canvasWidth, canvasHeight);
        physicalInstanceProcessor = new PhysicalInstanceProcessor();
        blocks = primitivesFactory.getStandartBlocks();
        canvas = primitivesFactory.getStandartCanvas();
        ballFirst = primitivesFactory.getStandartCircle();
        ballSecond = primitivesFactory.getStandartCircle();
        platformFirst = primitivesFactory.getStandartPlatform();
        platformSecond = primitivesFactory.getStandartPlatform();

        CollisionHandler collisionHandler = new GlobalCollisionHandler();
        collisionsFirst = new HashSet<>();
        collisionsFirst.add( new BodyPair(ballFirst, blocks, collisionHandler) ); //мяч-блоки
        collisionsFirst.add( new BodyPair(ballFirst, canvas, collisionHandler) ); //мяч-граница_канваса
        collisionsFirst.add( new BodyPair(ballFirst, platformFirst, collisionHandler) ); //мяч-платформа
        collisionsFirst.add( new BodyPair(platformFirst, canvas, collisionHandler) ); //платформа-граница_канваса

        collisionsSecond = new HashSet<>();
        collisionsSecond.add( new BodyPair(ballSecond, blocks, collisionHandler) ); //мяч-блоки
        collisionsSecond.add( new BodyPair(ballSecond, canvas, collisionHandler) ); //мяч-граница_канваса
        collisionsSecond.add( new BodyPair(ballSecond, platformSecond, collisionHandler) ); //мяч-платформа
        collisionsFirst.add( new BodyPair(platformSecond, canvas, collisionHandler) ); //платформа-граница_канваса
    }

    public IPhysicalInstanceProcessor getProcessor() {
        return physicalInstanceProcessor;
    }

    private class PhysicalInstanceProcessor implements IPhysicalInstanceProcessor {

        private void moveBall(Circle ball) {
            ball.setX(ball.getX() + ball.getVX());
            ball.setY(ball.getY() + ball.getVY());
        }

        private void movePlatform(Rectangle platform) {
            platform.setX(platform.getX() + platform.getVX());
        }

        private void redirrectPlatform(Rectangle platform, double vx) {
            platform.setVX(vx);
        }

        private COLLISION_TYPE getCollisionType(BodyPair bp) {
            Body first = bp.getFirst();
            Body second = bp.getSecond();

            if (first instanceof Rectangle) {
                return COLLISION_TYPE.PLATFORM_BOUND;
            }
            else if (first instanceof Circle) {
                if(second instanceof Blocks) {
                    return COLLISION_TYPE.BALL_BLOCKS;
                }
                if(second instanceof Canvas) {
                    return COLLISION_TYPE.BALL_BOUND;
                }
                if(second instanceof Rectangle) {
                    return COLLISION_TYPE.BALL_PLATFORM;
                }
            }
            return COLLISION_TYPE.UNKNOWN;
        }

        private boolean isTimeToStop() {
            return blocks.isDestroyed();
        }

        @Override
        public void redirrectPlatform(NUM_PLAYER numPlayer, double vx) {
            if(numPlayer == NUM_PLAYER.FIRST)
                redirrectPlatform(platformFirst, vx);
            else
                redirrectPlatform(platformSecond, vx);
        }

        @Override
        public void performGameStep() {
            moveBall(ballFirst);
            moveBall(ballSecond);
            movePlatform(platformFirst);
            movePlatform(platformSecond);
        }

        @Override
        public void processCollisions(IScoreProcessor scoreProcessor) {
            //noinspection Convert2streamapi
            for (BodyPair bp : collisionsFirst) {
                if(bp.onCollision()) {
                    COLLISION_TYPE collisionType = getCollisionType(bp);
                    if(collisionType == COLLISION_TYPE.BALL_BLOCKS)
                        scoreProcessor.scoresFirst();
                }
            }
            //noinspection Convert2streamapi
            for (BodyPair bp : collisionsSecond) {
                if(bp.onCollision()) {
                    COLLISION_TYPE collisionType = getCollisionType(bp);
                    if(collisionType == COLLISION_TYPE.BALL_BLOCKS)
                        scoreProcessor.scoresSecond();
                }
            }
        }

        @Override
        public void processStop(IStopProcessor stopProcessor) {
            if(isTimeToStop())
                stopProcessor.stop();
        }

        @Override
        public void getState(IGetStateProcessor processor) {
            JSONObject res = new JSONObject();

            JSONObject toFirst = new JSONObject();
            toFirst.put("your_ball", ballFirst.toJSON());
            toFirst.put("your_platform", platformFirst.toJSON());
            toFirst.put("another_ball", ballSecond.toJSON());
            toFirst.put("another_platform", platformSecond.toJSON());
            toFirst.put("blocks_matrix", blocks.toJSONArray());

            JSONObject toSecond = new JSONObject();
            toSecond.put("your_ball", ballSecond.toJSON());
            toSecond.put("your_platform", platformSecond.toJSON());
            toSecond.put("another_ball", ballFirst.toJSON());
            toSecond.put("another_platform", platformFirst.toJSON());
            toSecond.put("blocks_matrix", blocks.toJSONArray());

            res.put("toFirst", toFirst);
            res.put("toSecond", toSecond);

            processor.process(res);

        }//func

    }//innerclass

}//class
