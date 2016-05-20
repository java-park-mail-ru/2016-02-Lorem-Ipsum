package main;

import database.DbService;
import database.utils.FakeDbGenerator;
import frontend.RoutingServlet;
import game.gamemanagement.websocket.WebSocketGameServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.*;
import java.util.Properties;

public class Main {

    public static final int STANDART_PORT = 9090;
    public static final String STANDART_MYSQL_HOST = "localhost";
    public static final String STANDART_MYSQL_PORT = "3306";
    public static final String STANDART_MYSQL_DB_NAME = "dbJava";
    public static final String STANDART_MYSQL_LOGIN = "test";
    public static final String STANDART_MYSQL_PASSWORD = "test";
    public static final String STANDART_MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);

    @SuppressWarnings("OverlyComplexMethod")
    public static void main(String[] args) throws InterruptedException {
        int port;
        Properties serverProperties = readProperties("cfg/server.properties");
        Properties dbProperties = readProperties("cfg/db.properties");
        if (!serverProperties.containsKey("port")) {
            port = STANDART_PORT;
            System.out.append("Target port argument missed.\n");
            MAIN_LOGGER.warn("Target port argument missed. Start at port: {}", STANDART_PORT);
        }
        else {
            port = Integer.valueOf(serverProperties.getProperty("port"));
        }

        DbService accountService;

        try {
            String dbHost = dbProperties.contains("host") ?
                    dbProperties.getProperty("host") : Main.STANDART_MYSQL_HOST;
            String dbPort = dbProperties.contains("port") ?
                    dbProperties.getProperty("port") : Main.STANDART_MYSQL_PORT;
            String dbName = dbProperties.contains("name") ?
                    dbProperties.getProperty("name") : Main.STANDART_MYSQL_DB_NAME;
            String dbLogin = dbProperties.contains("login") ?
                    dbProperties.getProperty("login") : Main.STANDART_MYSQL_LOGIN;
            String dbPassword = dbProperties.contains("password") ?
                    dbProperties.getProperty("password") : Main.STANDART_MYSQL_PASSWORD;
            String dbDriver = dbProperties.contains("driver")
                    ? dbProperties.getProperty("driver") : Main.STANDART_MYSQL_DRIVER;
            accountService = new DbService(
                    dbHost,
                    dbPort,
                    dbName,
                    dbDriver,
                    dbLogin,
                    dbPassword
            );
            if(dbProperties.containsKey("fake") && dbProperties.getProperty("fake").equals("yes")) {
                FakeDbGenerator.generateDb(accountService);
            }
        }
        catch (RuntimeException e) {
            MAIN_LOGGER.error("Unable to init dbService. Reason: {}", e.getMessage());
            return;
        }

        Servlet routingServlet = new RoutingServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(routingServlet), "/api/v1/*");
        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountService, accountService)), "/gamesocket");


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        try {
            System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');
            server.start();
        } catch (Exception e) {
            System.out.append("Server wasn't started.\n");
            MAIN_LOGGER.error("Unable to start server. Error message: {}", e.getMessage());
            return;
        }
        server.join();
    }

    public static Properties readProperties(String path) {
        //noinspection OverlyBroadCatchBlock
        try (final FileInputStream fis = new FileInputStream(path)) {
            final Properties properties = new Properties();
            properties.load(fis);
            return properties;
        } catch (IOException e) {
            MAIN_LOGGER.warn("Unable to get properties : {}", path);
            return null;
        }
    }

    public static String readFromFile(String filePath) {
        File file = new File(filePath);
        try {

            if (!file.exists()) {
                return null;
            }

            char[] buffer = new char[(int)file.length()];
            try(FileReader fileReader = new FileReader(file))
            {
                fileReader.read(buffer);
                return new String(buffer);
            }
            catch (FileNotFoundException e){
                System.out.append(e.getMessage());
                return null;
            }
        }
        catch (IOException e) {
            System.out.append(e.getMessage());
            return null;
        }
    }

}