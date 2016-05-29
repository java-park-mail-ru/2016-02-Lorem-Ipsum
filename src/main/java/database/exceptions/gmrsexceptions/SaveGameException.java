package database.exceptions.gmrsexceptions;

import database.exceptions.GameResultsException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class SaveGameException extends GameResultsException {

    public SaveGameException(HibernateException ex) {
        super(ex);
    }

}
