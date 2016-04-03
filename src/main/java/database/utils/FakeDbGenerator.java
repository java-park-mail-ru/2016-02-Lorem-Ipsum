package database.utils;

import database.DbService;
import main.Main;
import main.UserProfile;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by Installed on 03.04.2016.
 */
public class FakeDbGenerator {

    public static final String PATH_TO_USERS = "testData/dbData/users.json";
    public static final String PATH_TO_RESULTS = "testData/dbData/results.json";

    public static void generateDb(DbService dbService) {
        String usersStrJSON = Main.readFromFile(PATH_TO_USERS);
        String resultsStrJSON = Main.readFromFile(PATH_TO_RESULTS);
        if(usersStrJSON != null && resultsStrJSON != null) {
            JSONArray usersJSONArray = new JSONArray(usersStrJSON);
            JSONArray resultsJSONArray = new JSONArray(resultsStrJSON);
            for (Object user : usersJSONArray) {
                JSONObject userJSONObj = (JSONObject) user;
                UserProfile userProfile = new UserProfile(userJSONObj);
                dbService.addUser(userProfile);
            }
            for (Object result : resultsJSONArray) {
                JSONObject resultJSONObj = (JSONObject) result;
                String userLogin = resultJSONObj.getString("login");
                long score = resultJSONObj.getLong("score");
                dbService.saveGameResultByUserLogin(userLogin, score);
            }//for
        }//if
    }//func

}
