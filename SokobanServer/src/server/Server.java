package server;

import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import server.routes.LevelsHandler;
import server.routes.LoginHandler;
import server.routes.RegisterHandler;

/**
 *
 * @author ridhv
 */
public class Server {

    public static void startServer(int port) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        //  Add routes
        server.createContext("/api/login", new LoginHandler());
        server.createContext("/api/register", new RegisterHandler());
        server.createContext("/api/levels", new LevelsHandler());
        server.setExecutor(null); // creates a default executor
        //  Start server
        server.start();
        System.out.println("Server started at http://127.0.0.1:" + port);
    }

    public static void main(String[] args) throws Exception {
        DBConnector.initialise();
        startServer(8000);
    }
}