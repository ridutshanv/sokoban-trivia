package server.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class BaseHandler {

    public static Gson jsonParser = new Gson();

    //  Usually, we only accept POST requests so we can consider this the default
    public boolean validateRequestMethod(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();

        if (!(method.equalsIgnoreCase("POST"))) {
            this.sendResponse(exchange, 400, "Method " + method + " not allowed.");
            return false;
        }

        return true;
    }

    //  In case we need to specify another HTTP request method, we can use this function (method overloading)
    public boolean validateRequestMethod(HttpExchange exchange, String allowedMethod) throws IOException {
        String method = exchange.getRequestMethod();

        if (!(method.equalsIgnoreCase(allowedMethod))) {
            this.sendResponse(exchange, 400, "Method " + method + " not allowed.");
            return false;
        }

        return true;
    }

    public JsonObject getPayload(HttpExchange exchange) throws IOException {
        StringBuilder sb;
        //  The InputStreamReader will automatically close when it exits this try-block
        try ( InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "utf-8")) {
            BufferedReader br = new BufferedReader(isr);
            sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
        }
        return jsonParser.fromJson(sb.toString(), JsonObject.class);
    }

    public void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}
