package datacheck;

public class ElementaryChecker {

    public static boolean checkUserLogin(String userLogin) {
        return userLogin != null;
    }

    public static boolean checkUserPassword(String userPassword) {
        return userPassword != null;
    }

    public static boolean checkUserEmail(String userEmail) {
        return userEmail != null;
    }

    public static boolean checkSessionId(String sessionId) {
        return sessionId != null;
    }

    public static boolean checkUserId(Long userId) { return userId != null; }

}
