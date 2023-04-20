package server;

import java.util.regex.Pattern;

public class Utils {

    public static boolean patternMatches(String comparisonString, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(comparisonString)
                .matches();
    }
}