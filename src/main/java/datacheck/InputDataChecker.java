package datacheck;

import org.jetbrains.annotations.Nullable;

public class InputDataChecker {

    public static boolean checkSignUp(String userLogin, String userPassword, String userEmail) {
        return ElementaryChecker.checkUserLogin(userLogin) && ElementaryChecker.checkUserPassword(userPassword)
                && ElementaryChecker.checkUserEmail(userEmail);
    }

    public static boolean checkChangeUser(String userLogin, String userPassword, String userEmail) {
        return checkSignUp(userLogin, userPassword, userEmail);
    }

    public static boolean checkSignIn(String userLogin, String userPassword, String sessionId) {
        return ElementaryChecker.checkUserLogin(userLogin) && ElementaryChecker.checkUserPassword(userPassword)
                && ElementaryChecker.checkSessionId(sessionId);
    }
}
