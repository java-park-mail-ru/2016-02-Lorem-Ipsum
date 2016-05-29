package database.exceptions.userexceptions;

import database.exceptions.UserException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class GetUserException extends UserException {

    public GetUserException(HibernateException ex) {
        super(ex);
    }

}
