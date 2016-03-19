package main;

//import com.sun.javafx.scene.control.skin.VirtualFlow;
import frontend.RoutingServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class Main {

    public static final int STANDART_PORT = 9090;

    public static final Logger MAIN_LOGGER = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws InterruptedException {
        int port;
        if (args.length != 1) {
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

        System.out.append("Starting at port: ").append(String.valueOf(port)).append('\n');

        AccountService accountService = new AccountService();
        Servlet routingServlet = new RoutingServlet(accountService);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(routingServlet), "/api/v1/*");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase("public_html");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});

        Server server = new Server(port);
        server.setHandler(handlers);

        try {
            server.start();
        } catch (Exception e) {
            System.out.append("Server wasn't started.\n");
            MAIN_LOGGER.error("Unable to start server. Error message: {}", e.getMessage());
            return;
        }
        server.join();
    }
}