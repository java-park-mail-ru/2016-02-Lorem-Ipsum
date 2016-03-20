package database;

import com.sun.org.apache.xalan.internal.xsltc.compiler.util.TestGenerator;
import main.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Installed on 20.03.2016.
 */
public class DbRebuilder {

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    public static void main(String[] args) throws InterruptedException {
        rebuildDb(
                Main.STANDART_MYSQL_HOST,
                Main.STANDART_MYSQL_PORT,
                Main.STANDART_MYSQL_DB_NAME,
                Main.STANDART_MYSQL_LOGIN,
                Main.STANDART_MYSQL_PASSWORD
        );
    }

    public static void rebuildDb(String hostname, String port, String dbName, String login, String password) {

        Connection connection = null;
        PreparedStatement usersStatement = null;
        PreparedStatement sessionStatement = null;

        try {
            String createUsersTableSqlString = Main.readFromFile("dbrequests/CreateUsersTable").replace("\r\n","");
            String createSessionsTableSqlString = Main.readFromFile("dbrequests/CreateSessionsTable").replace("\r\n","");
            connection = DbConnector.getConnectionFromPool(hostname, port, dbName, login, password);
            usersStatement = connection.prepareStatement(createUsersTableSqlString);
            sessionStatement = connection.prepareStatement(createSessionsTableSqlString);
            LOGGER.debug("Execution: " + createUsersTableSqlString);
            LOGGER.debug("Execution: " + createSessionsTableSqlString);
            PreparedStatement[] toExecute = {usersStatement, sessionStatement};
            TExecutor.execUpdate(connection, toExecute);
        }
        catch (SQLException | NullPointerException e) {
            LOGGER.debug("Error: " + e.getMessage());
        }
        finally {
                try {
                    if(connection != null)
                        connection.close();
                    if(usersStatement != null)
                        usersStatement.close();
                    if(sessionStatement != null)
                        sessionStatement.close();
                }
                catch (SQLException e) {
                    LOGGER.debug("Error while closing : " + e.getMessage());
                }
        }//try-catch

    }//func

}
