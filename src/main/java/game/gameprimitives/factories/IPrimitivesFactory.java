package game.gameprimitives.factories;

import game.gameprimitives.Blocks;
import game.gameprimitives.Canvas;
import game.gameprimitives.Circle;
import game.gameprimitives.Rectangle;

/**
 * Created by Installed on 09.05.2016.
 */
public interface IPrimitivesFactory {
    Rectangle getStandartPlatform();
    Circle getStandartCircle();
    Blocks getStandartBlocks();
    Canvas getStandartCanvas();
}
