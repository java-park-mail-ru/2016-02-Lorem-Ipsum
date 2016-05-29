package database.exceptions;

import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class SessionException extends DbServiceException {

    public SessionException(HibernateException ex) {
        super(ex);
    }
}
