package server.routes;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import static server.Utils.patternMatches;
import server.DBConnector;

public class RegisterHandler extends BaseHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        boolean valid = super.validateRequestMethod(exchange, "POST");  //  Only allow POST requests

        if (valid == true) {
            JsonObject payload = super.getPayload(exchange);

            String username = payload.get("username").getAsString();
            String email = payload.get("email").getAsString();
            String password = payload.get("password").getAsString();
            String confirmPassword = payload.get("confirmPassword").getAsString();

            System.out.println("Username: " + username + " | Email: " + email + " | Password: " + password + " | Confirmation Password: " + confirmPassword);

            //  Validate whether the fields are empty
            int usernameLength = username.length();
            if (usernameLength == 0) {
                super.sendResponse(exchange, 400, "You need to enter a username.");
                return;
            }

            if ((email.length()) == 0) {
                super.sendResponse(exchange, 400, "You need to provide an e-mail address.");
                return;
            }

            if (password.length() == 0) {
                super.sendResponse(exchange, 400, "You need to provide a password.");
                return;
            }

            if (confirmPassword.length() == 0) {
                super.sendResponse(exchange, 400, "You need to provide the second, confirmation password.");
                return;
            }

            //  Username validation
            if ((usernameLength < 3) || (usernameLength > 18)) {
                super.sendResponse(exchange, 400, "Your username length needs to be between 3-18 characters.");
                return;
            }

            if (DBConnector.fetchUserByUsername(username)) {
                super.sendResponse(exchange, 400, "A user with that username already exists, please try another or proceed to login.");
                return;
            }

            /*
                Email validation

                Uses the Regular Expression by RFC 5322 for email validation.
                Source: https://www.baeldung.com/java-email-validation-regex#regular-expression-by-rfc-5322-for-email-validation
             */
            String emailRegexPattern = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
            if (!(patternMatches(email, emailRegexPattern))) {
                super.sendResponse(exchange, 400, "Please enter a valid e-mail address.");
                return;
            }

            if (DBConnector.fetchUserByEmail(email)) {
                super.sendResponse(exchange, 400, "A user with that e-mail address already exists, please enter another.");
                return;
            }

            /*
                Passsword validation

                Source: https://uibakery.io/regex-library/password-regex-java
             */
            if (!(password.equals(confirmPassword))) {
                super.sendResponse(exchange, 400, "Your passwords need to match.");
                return;
            }

            String passwordRegexPattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,20}$";
            if (!(patternMatches(password, passwordRegexPattern))) {
                super.sendResponse(exchange, 400, "Your password needs to be of length 6-20, consisting of at least one special, numeric, uppercase & lowercase character.");
                return;
            }

            //  Insert user
            if (!(DBConnector.addUser(username, email, password))) {
                super.sendResponse(exchange, 400, "Could not create your account. Please try again with different credentials, or try later.");
                return;
            }

            // Success
            super.sendResponse(exchange, 200, "Success! Your account under the username " + username + " has been created!");
        }
    }
}