package server.routes;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import server.DBConnector;

public class LoginHandler extends BaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean valid = super.validateRequestMethod(exchange, "POST");  //  Only allow POST requests

        if (valid == true) {
            JsonObject payload = super.getPayload(exchange);

            String username = payload.get("username").getAsString();
            String password = payload.get("password").getAsString();

            if (username.length() == 0) {
                super.sendResponse(exchange, 400, "Please enter a valid username.");
                return;
            }

            if (password.length() == 0) {
                super.sendResponse(exchange, 400, "Please enter a valid password.");
                return;
            }

            if (DBConnector.fetchUser(username, password)) {
                //  If user exists
                super.sendResponse(exchange, 200, "User " + username + " has successfully logged in.");
            } else {
                super.sendResponse(exchange, 400, "Sorry! Incorrect username or password provided.");
            }
        }
    }
}