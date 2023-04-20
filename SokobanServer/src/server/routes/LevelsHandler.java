package server.routes;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;

public class LevelsHandler extends BaseHandler implements HttpHandler {

    private static final HashMap levels = new HashMap<Integer, char[][]>() {
        {
            //  Level 1
            put(1, new char[][]{{'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'},
            {'w', 'w', ' ', ' ', ' ', ' ', 'w', 'w', 'w', 'w'},
            {'w', ' ', ' ', ' ', ' ', ' ', ' ', 'b', 'w', 'w'},
            {'w', ' ', ' ', ' ', 'w', ' ', ' ', 'w', 'w', 'w'},
            {'w', ' ', 'c', ' ', 'w', ' ', ' ', ' ', 'w', 'w'},
            {'w', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', 'w'},
            {'w', ' ', ' ', ' ', ' ', ' ', ' ', 'g', ' ', 'w'},
            {'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'}});
            //  Level 2
            put(2, new char[][]{{'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'},
            {'w', ' ', ' ', ' ', ' ', ' ', 'w', 'w', 'w', 'w'},
            {'w', ' ', ' ', 'w', ' ', ' ', ' ', 'b', 'w', 'w'},
            {'w', ' ', ' ', ' ', 'c', ' ', 'w', 'w', 'w', 'w'},
            {'w', 'g', ' ', ' ', ' ', ' ', 'w', ' ', ' ', 'w'},
            {'w', ' ', 'c', ' ', ' ', ' ', ' ', ' ', ' ', 'w'},
            {'w', ' ', ' ', ' ', 'w', 'w', 'w', ' ', 'b', 'w'},
            {'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'}});
            //  Level 3
            put(3, new char[][]{{'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'},
            {'w', ' ', ' ', ' ', ' ', ' ', ' ', 'b', 'w', 'w'},
            {'w', ' ', ' ', 'b', ' ', ' ', ' ', 'w', 'w', 'w'},
            {'w', ' ', ' ', 'w', ' ', ' ', ' ', ' ', ' ', 'w'},
            {'w', ' ', ' ', 'w', ' ', ' ', ' ', 'c', ' ', 'w'},
            {'w', ' ', ' ', ' ', ' ', 'w', ' ', 'c', ' ', 'w'},
            {'w', ' ', 'b', ' ', ' ', ' ', ' ', 'c', 'g', 'w'},
            {'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w', 'w'}});
        }
    };

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean valid = super.validateRequestMethod(exchange, "POST");  //  Only allow POST requests

        if (valid == true) {
            JsonObject payload = super.getPayload(exchange);

            int levelNum = payload.get("level").getAsInt();
            //  Get the requested level from the levels HashMap, or default to the first level if it doesn't exist
            char[][] level = (char[][]) levels.getOrDefault(levelNum, 1);
            //  Return the level as a String
            super.sendResponse(exchange, 200, Arrays.deepToString(level));
        }
    }
}