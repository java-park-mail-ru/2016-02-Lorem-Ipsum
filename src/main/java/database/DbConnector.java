package database;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Installed on 20.03.2016.
 */
public class DbConnector {

    public static final Logger LOGGER = LogManager.getLogger("DbLogger");

    private static BasicDataSource connectionPool = null;

    public static Connection getConnectionFromPool(String hostname, String port, String dbName, String login, String password) {
        try {
            LOGGER.debug("Trying to connect to db from pool");
            if(connectionPool != null)
                return connectionPool.getConnection();
            LOGGER.debug("Pool does not exist. Trying to create new one.");
            connectionPool = new BasicDataSource();
            connectionPool.setDriverClassName("com.mysql.jdbc.Driver");
            StringBuilder url = new StringBuilder();
            url.
                    append("jdbc:mysql://").
                    append(hostname).append(":").
                    append(port).append("/").
                    append(dbName);
            connectionPool.setUrl(url.toString());
            connectionPool.setUsername(login);
            connectionPool.setPassword(password);
            LOGGER.debug("Pool configured. Login: {}, Password: {}, URL: {}.", login, password, url.toString());
            return connectionPool.getConnection();
        }
        catch (SQLException e) {
            LOGGER.debug("Failed to connect to db from pool. Reason:" + e.getMessage());
        }
        return null;
    }

    public static Connection getConnection(String hostname, String port, String dbName, String login, String password) {
        try {
            DriverManager.registerDriver((Driver) Class.forName("com.mysql.jdbc.Driver").newInstance());

            StringBuilder url = new StringBuilder();

            url.
                    append("jdbc:mysql://").        //db type
                    append(hostname).append(":").            //host name
                    append(port).append("/").                //port
                    append(dbName).append("?").           //db name
                    append("user=").append(login).append("&").            //login
                    append("password=").append(password);        //password

            LOGGER.debug("Trying to connect to db by url: " + url.toString());

            Connection connection = DriverManager.getConnection(url.toString());

            LOGGER.debug("Success. Connection info: " + getConnectionInfo(connection).toString());

            return connection;

        }
        catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOGGER.error("Failed to connect to database. Reason: " + e.getMessage());
        }
        return null;
    }

    public static StringBuilder getConnectionInfo(Connection connection) {
        StringBuilder output = new StringBuilder();
        try {
            output.append(" Autocommit: ").append(connection.getAutoCommit()).append(";")
                    .append(" DbName: ").append(connection.getMetaData().getDatabaseProductName()).append(";")
                    .append(" DbVersion: ").append(connection.getMetaData().getDatabaseProductVersion()).append(";")
                    .append(" DriverName: ").append(connection.getMetaData().getDriverName()).append(";")
                    .append(" DriverVersion: ").append(connection.getMetaData().getDriverVersion()).append(";")
                    .append(" UserName: ").append(connection.getMetaData().getUserName());
        }
        catch (SQLException e) {
            LOGGER.error(e.getMessage());
            return null;
        }
        return output;
    }

}
