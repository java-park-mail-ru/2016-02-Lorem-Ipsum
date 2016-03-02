package datacheck;

import com.sun.istack.internal.Nullable;

public class ElementaryGetter {

    @Nullable
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
