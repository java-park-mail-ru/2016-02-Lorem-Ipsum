package datacheck;

public class ElementaryGetter {

    public static Long getLongOrNull (String sUserId) {
        if(sUserId == null)
            return null;
        Long userId;
        try {
            userId = Long.parseLong(sUserId);
        } catch (NumberFormatException e) {
            return null;
        }
        return userId;
    }
}
