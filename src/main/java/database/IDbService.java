package database;

import main.IAccountService;
import main.UserProfile;

/**
 * Created by Installed on 26.03.2016.
 */
public interface IDbService extends IAccountService {

    public void close();

}
