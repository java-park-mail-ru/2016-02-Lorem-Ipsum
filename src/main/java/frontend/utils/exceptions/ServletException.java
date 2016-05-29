package frontend.utils.exceptions;

/**
 * Created by Installed on 29.05.2016.
 */
public class ServletException extends Exception {

    public ServletException(String message) {
        super(message);
    }

    public ServletException(Exception ex) {
        super(ex);
    }

}
