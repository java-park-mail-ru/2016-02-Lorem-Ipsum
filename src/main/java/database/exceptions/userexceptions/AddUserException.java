package database.exceptions.userexceptions;

import database.exceptions.UserException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class AddUserException extends UserException {

    public AddUserException(HibernateException ex) {
        super(ex);
    }

}
