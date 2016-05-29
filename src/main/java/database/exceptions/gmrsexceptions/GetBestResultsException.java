package database.exceptions.gmrsexceptions;

import database.exceptions.GameResultsException;
import org.hibernate.HibernateException;

/**
 * Created by Installed on 29.05.2016.
 */
public class GetBestResultsException extends GameResultsException {

    public GetBestResultsException(HibernateException ex) { super(ex); }

}
