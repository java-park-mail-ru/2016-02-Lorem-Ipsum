package main;

import database.DbService;
import database.utils.FakeDbGenerator;
import frontend.RoutingServlet;
import game.websocket.WebSocketGameServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    public static final int STANDART_PORT = 9090;
    public static final String STANDART_MYSQL_HOST = "localhost";
    public static final String STANDART_MYSQL_PORT = "3306";
    public static final String STANDART_MYSQL_DB_NAME = "dbJava";
    public static final String STANDART_MYSQL_LOGIN = "test";
    public static final String STANDART_MYSQL_PASSWORD = "test";
    public static final String STANDART_MYSQL_DRIVER = "com.mysql.jdbc.Driver";

    public static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int port;

        if (args.length < 1) {
            port = STANDART_PORT;
            System.out.append("Target port argument missed.\n");
            MAIN_LOGGER.warn("Target port argument missed. Start at port: {}", STANDART_PORT);
        }
        else {
            try{
                port = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                port = STANDART_PORT;
                System.out.append("Incorrect value of target port.");
                MAIN_LOGGER.warn("Unable to get the vslue of port. Start at port: {}", STANDART_PORT);
            }
        }

        DbService accountService;
        try {
            accountService = new DbService(
                    Main.STANDART_MYSQL_HOST,
                    Main.STANDART_MYSQL_PORT,
                    Main.STANDART_MYSQL_DB_NAME,
                    Main.STANDART_MYSQL_DRIVER,
                    Main.STANDART_MYSQL_LOGIN,
                    Main.STANDART_MYSQL_PASSWORD
            );
            if(args.length >= 2 && args[1].equals("fake")) {
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
        context.addServlet(new ServletHolder(new WebSocketGameServlet(accountService, accountService)), "/gameplay");


        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("static");

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