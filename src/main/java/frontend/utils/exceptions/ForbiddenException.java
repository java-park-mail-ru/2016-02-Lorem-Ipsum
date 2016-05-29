package frontend.utils.exceptions;

/**
 * Created by Installed on 29.05.2016.
 */
public class ForbiddenException extends ServletException {

    public ForbiddenException(String message) {
        super(message);
    }

    public ForbiddenException(Exception ex) {
        super(ex);
    }
}
