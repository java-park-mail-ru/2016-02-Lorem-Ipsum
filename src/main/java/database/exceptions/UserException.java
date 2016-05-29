package database.exceptions;

import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class UserException extends DbServiceException {

    public UserException(HibernateException ex) {
        super(ex);
    }

}
