package game.tmpgame;

import org.json.JSONArray;

/**
 * Created by Installed on 11.05.2016.
 */
public class GameState {

    int[][] matrix;
    int rows;
    int columns;

    public GameState(int r, int c) {
        this.rows = r;
        this.columns = c;
        matrix = new int[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = 1;
            }
        }
    }

    public JSONArray toJSON_me() {
        return toJSON(matrix);
    }

    public JSONArray toJSON(int[][] inputMtr) {
        JSONArray mtrJSON = new JSONArray();
        for (int i = 0; i < rows; i++){
            JSONArray row = new JSONArray();
            for (int j = 0; j < columns; j++){
                row.put(j, inputMtr[i][j]);
            }
            mtrJSON.put(i, row);
        }
        return mtrJSON;
    }

    public JSONArray conjunct(JSONArray inputMtrJSON) {
        try {
            for (int i = 0; i < rows; i++){
                JSONArray row = inputMtrJSON.getJSONArray(i);
                for (int j = 0; j < columns; j++){
                    int tmp = row.getInt(j);
                    matrix[i][j] = Math.min(matrix[i][j], tmp);
                }
            }
            return toJSON_me();
        }
        catch (Exception ex) {
            return null;
        }
    }

}
