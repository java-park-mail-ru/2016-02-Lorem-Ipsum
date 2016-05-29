package database.exceptions;

import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class DbServiceException extends HibernateException {

    public DbServiceException(HibernateException ex) {
        super(ex);
    }

}
