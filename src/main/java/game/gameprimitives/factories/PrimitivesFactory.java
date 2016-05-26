package game.gameprimitives.factories;

import game.gameprimitives.Blocks;
import game.gameprimitives.Canvas;
import game.gameprimitives.Circle;
import game.gameprimitives.Rectangle;

/**
 * Created by Installed on 09.05.2016.
 */
@SuppressWarnings({"CanBeFinal", "MagicNumber", "FieldCanBeLocal", "InstanceVariableNamingConvention", "NonConstantFieldWithUpperCaseName"})
public class PrimitivesFactory implements IPrimitivesFactory {

    private double canvasWidth;
    private double canvasHeight;

    public PrimitivesFactory(double canvasWidth, double canvasHeight) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
    }


    private final int PLATFORM_OFFSET_FACTOR = 2;

    public Rectangle getStandartRectangle() {
        return new Rectangle(
                (canvasWidth - 40)/2,
                canvasHeight - PLATFORM_OFFSET_FACTOR * 5,
                0,
                0,
                40,
                4
        );
    }

    @Override
    public Rectangle getStandartPlatform() {
        return getStandartRectangle();
    }

    @Override
    public Canvas getStandartCanvas() {
        return new Canvas(0, 0, canvasWidth, canvasHeight);
    }

    @Override
    public Circle getStandartCircle() {
        return new Circle(
                canvasWidth - 50,
                canvasHeight - 30,
                2,
                3,
                5
        );
    }


    private final int BLOCK_ROWS = 4;
    private final int BLOCK_COLUMNS = 20;

    @Override
    public Blocks getStandartBlocks() {
        return new Blocks(
                0,
                0,
                0,
                0,
                canvasWidth,
                BLOCK_ROWS * (15 + 1),
                BLOCK_ROWS,
                BLOCK_COLUMNS,
                2,
                1,
                15,
                15,
                (canvasWidth - 15 * BLOCK_COLUMNS) / 3, //to improve
                1
        );
    }

}
