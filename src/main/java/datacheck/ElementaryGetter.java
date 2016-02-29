package datacheck;

import org.jetbrains.annotations.Nullable;

public class ElementaryGetter {

    @Nullable
    public static Long getLongOrNull (String sUserId) {
        if(sUserId == null)
            return null;
        Long userId = null;
        try {
            userId = Long.parseLong(sUserId);
        } catch (NumberFormatException e) {
            return null;
        }
        return userId;
    }
}
