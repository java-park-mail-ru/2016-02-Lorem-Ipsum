package database.exceptions.userexceptions;

import database.exceptions.UserException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class DeleteUserException extends UserException {

    public DeleteUserException(HibernateException ex) {
        super(ex);
    }

}
