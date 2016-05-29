package database.exceptions.sesexceptions;

import database.exceptions.SessionException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class SessionExistsException extends SessionException {

    public SessionExistsException(HibernateException ex) {
        super(ex);
    }

}
