package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Installed on 20.03.2016.
 */
public class DbConnector {

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    private static org.apache.tomcat.jdbc.pool.DataSource connectionPoolRes = null;

    public DbConnector(String hostname, String port, String dbName, String driverName,
                                                   String login, String password) {
        try {
            LOGGER.debug("Trying to init tomcat pool");
            if(connectionPoolRes != null)
                return;
            LOGGER.debug("Pool does not exist. Trying to create new one.");
            PoolProperties poolProperties = new PoolProperties();
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").
                    append(hostname).append(':').
                    append(port).append('/').
                    append(dbName);
            poolProperties.setUrl(url.toString());
            poolProperties.setDriverClassName(driverName);
            poolProperties.setUsername(login);
            poolProperties.setPassword(password);
            connectionPoolRes = new org.apache.tomcat.jdbc.pool.DataSource();
            connectionPoolRes.setPoolProperties(poolProperties);
            LOGGER.debug("Pool configured. Login: {}, Password: {}, URL: {}.", login, password, url.toString());
        }
        catch (RuntimeException e) {
            LOGGER.debug("Failed to init connection pool. Reason:" + e.getMessage());
        }
    }

    public Connection getConnectionFromPool() throws SQLException {
        return connectionPoolRes.getConnection();
    }

    public DataSource getDataSource() {
        return connectionPoolRes;
    }

    public static Connection getSingleConnection(String hostname, String port, String dbName, String driverName,
                                           String login, String password) {
        try {
            DriverManager.registerDriver((Driver) Class.forName(driverName).newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append(hostname).append(':').            //host name
                    append(port).append('/').                //port
                    append(dbName).append('?').           //db name
                    append("user=").append(login).append('&').            //login
                    append("password=").append(password);        //password

            LOGGER.debug("Trying to connect to db by url: " + url);

            Connection connection = DriverManager.getConnection(url.toString());

            LOGGER.debug("Success. Connection info: " + getConnectionInfo(connection));

            return connection;

        }
        catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException | NullPointerException e) {
            LOGGER.error("Failed to connect to database. Reason: " + e.getMessage());
        }
        return null;
    }

    public static StringBuilder getConnectionInfo(Connection connection) {
        StringBuilder output = new StringBuilder();
        try {
            output.append(" Autocommit: ").append(connection.getAutoCommit()).append(';')
                    .append(" DbName: ").append(connection.getMetaData().getDatabaseProductName()).append(';')
                    .append(" DbVersion: ").append(connection.getMetaData().getDatabaseProductVersion()).append(';')
                    .append(" DriverName: ").append(connection.getMetaData().getDriverName()).append(';')
                    .append(" DriverVersion: ").append(connection.getMetaData().getDriverVersion()).append(';')
                    .append(" UserName: ").append(connection.getMetaData().getUserName());
        }
        catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return output;
    }

}
