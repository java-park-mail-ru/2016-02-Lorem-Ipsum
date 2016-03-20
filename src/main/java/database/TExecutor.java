package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

/**
 * Created by Installed on 20.03.2016.
 */
public class TExecutor {

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    public <T> T execQuery(Connection connection,
                           String query,
                           ITResultHandler<T> handler)
            throws SQLException {

        Statement stmt = null;
        ResultSet result = null;
        T value = null;

        try {
            stmt = connection.createStatement();
            stmt.execute(query);
            LOGGER.debug("Executed: {} with connection {}" , query, DbConnector.getConnectionInfo(connection));
            result = stmt.getResultSet();
            value = handler.handle(result);
            return value;
        }
        finally {
            if(stmt != null)
                stmt.close();
            if(result != null)
                result.close();
        }
    }

    public static void execUpdate(Connection connection, PreparedStatement[] updates) {
        try {
            connection.setAutoCommit(false);
            for (PreparedStatement update : updates) {
                update.executeUpdate();
                LOGGER.debug("Executed prepared statement with connection " + DbConnector.getConnectionInfo(connection));
            }
            connection.commit();
        }
        catch (SQLException e) {
            try {
                LOGGER.debug("Execution of prepared statement failed with error " + e.getMessage()
                        + " in connection " + DbConnector.getConnectionInfo(connection));
                connection.rollback();
                LOGGER.debug("Transaction is rolled back in" + DbConnector.getConnectionInfo(connection));
                connection.setAutoCommit(true);
            }
            catch (SQLException ignore) {
                LOGGER.debug("Failed to finish transaction. Error: " + ignore.getMessage());
            }
        }
    }

}
