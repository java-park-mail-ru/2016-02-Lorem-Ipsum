package database.exceptions.sesexceptions;

import database.exceptions.SessionException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class DeleteSessionException extends SessionException {

    public DeleteSessionException(HibernateException ex) {
        super(ex);
    }

}
