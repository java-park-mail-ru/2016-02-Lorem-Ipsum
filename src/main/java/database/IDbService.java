package database;

import main.IAccountService;

/**
 * Created by Installed on 26.03.2016.
 */
public interface IDbService extends IAccountService {

    void close();

}
