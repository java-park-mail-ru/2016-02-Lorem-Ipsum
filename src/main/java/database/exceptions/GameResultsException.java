package database.exceptions;

import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class GameResultsException extends DbServiceException {

    public GameResultsException(HibernateException ex) {
        super(ex);
    }

}
