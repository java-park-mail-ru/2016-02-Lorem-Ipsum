package database.exceptions.userexceptions;

import database.exceptions.UserException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class UserExistsException extends UserException {
    public UserExistsException(HibernateException ex) {
        super(ex);
    }
}
