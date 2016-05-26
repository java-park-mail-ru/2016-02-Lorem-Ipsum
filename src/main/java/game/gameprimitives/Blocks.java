package game.gameprimitives;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Blocks extends Rectangle {

    private int[][] hitPointMatrix;

    private int rowsCount;
    private int columnsCount;

    private double paddingX;
    private double paddingY;
    private int blockWidth;
    private int blockHeight;
    private double offsetX;
    private double offsetY;

    public double getPaddingX() { return paddingX; }
    public void setPaddingX(double paddingX) { this.paddingX = paddingX;}
    public double getPaddingY() { return paddingY; }
    public void setPaddingY(double paddingY) { this.paddingY = paddingY;}
    public int getBlockWidth() {return blockWidth;}
    public void setBlockWidth(int blockWidth) {this.blockWidth = blockWidth;}
    public int getBlockHeight() {return blockHeight;}
    public void setBlockHeight(int blockHeight) {this.blockHeight = blockHeight;}
    public double getOffsetX() { return offsetX; }
    public void setOffsetX(double offsetX) { this.offsetX = offsetX;}
    public double getOffsetY() { return offsetY; }
    public void setOffsetY(double offsetY) { this.offsetY = offsetY;}


    private boolean isCorrectRow(int row){
        return ( row >= 0 && row < rowsCount);
    }
    private boolean isCorrectColumn (int column){
        return ( column >= 0 && column < columnsCount);
    }
    public void destroyBlock(int row,int column){
        if (isCorrectRow(row) && isCorrectColumn(column)){
            hitPointMatrix[row][column] = 0;
        }
        else{
            LOGGER.debug("Invalid row or column");
        }

    }
    public boolean attackBlock(int row,int column){
        if (isCorrectRow(row) && isCorrectColumn(column)){
            if(hitPointMatrix[row][column] > 0) {
                hitPointMatrix[row][column]--;
                return true;
            }
            else
                return false;
        }
        else{
            LOGGER.debug("Invalid row or column");
            return false;
        }
    }

    public void setHitPoints( int[][] matrix){
        hitPointMatrix = matrix;
    }

    public Blocks(
            double x,
            double y,
            double vx,
            double vy,
            double w,
            double h,
            int r,
            int c,
            double paddingX,
            double paddingY,
            int blockWidth,
            int blockHeight,
            double offsetX,
            double offsetY
    ){
        super(x,y,vx,vy,w,h);
        rowsCount = r;
        columnsCount = c;
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        this.blockWidth = blockWidth;
        this.blockHeight = blockHeight;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        hitPointMatrix = new int[rowsCount][columnsCount];
        for (int i = 0; i < rowsCount; i++){
            for (int j = 0; j < columnsCount; j++){
                hitPointMatrix[i][j] = 1;
            }
        }

    }

    public boolean isDestroyed() {
        int blockExists = 0;
        for (int i = 0; i < rowsCount; i++){
            for (int j = 0; j < columnsCount; j++){
                blockExists |= hitPointMatrix[i][j];
            }
        }
        return blockExists == 0;
    }

    public Blocks (JSONObject blocks){
        super(blocks);
        try {
            rowsCount = blocks.has("rows") ? blocks.getInt("rows") : 0;
            columnsCount = blocks.has("columns") ? blocks.getInt("columns") : 0;
            blockWidth = blocks.has("blockWidth") ? blocks.getInt("blockWidth") : 0;
            blockHeight = blocks.has("blockHeight") ? blocks.getInt("blockHeight") : 0;
            paddingX = blocks.has("paddingX") ? blocks.getDouble("paddingX") : 0;
            paddingY = blocks.has("paddingY") ? blocks.getDouble("paddingY") : 0;
            offsetX = blocks.has("offsetX") ? blocks.getDouble("offsetX") : 0;
            offsetY = blocks.has("offsetY") ? blocks.getDouble("offsetY") : 0;
        }
        catch(JSONException err){
            LOGGER.debug(err.getMessage());
        }
    }

    public JSONArray toJSONArray() {
        JSONArray rows = new JSONArray();
        for (int i=0; i < rowsCount; i++){
            JSONArray row = new JSONArray();
            for (int j=0; j < columnsCount; j++){
                row.put(j, hitPointMatrix[i][j]);
            }
            rows.put(i, row);
        }
        return rows;
    }

    @SuppressWarnings("CloneDoesntCallSuperClone")
    @Override
    public Body clone() {
        Blocks blocks = new Blocks(
                super.getX(),
                super.getY(),
                super.getVX(),
                super.getVY(),
                super.getWidth(),
                super.getHeight(),
                rowsCount,
                columnsCount,
                paddingX,
                paddingY,
                blockWidth,
                blockHeight,
                offsetX,
                offsetY
        );
        int[][] hitPointMatrixCopy = hitPointMatrix.clone();
        blocks.hitPointMatrix = hitPointMatrixCopy;
        return blocks;
    }

}
