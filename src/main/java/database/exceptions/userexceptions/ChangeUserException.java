package database.exceptions.userexceptions;

import database.exceptions.UserException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class ChangeUserException extends UserException {

    public ChangeUserException(HibernateException ex) { super(ex); }

}
