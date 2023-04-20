package client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.http.HttpClient;
import java.util.regex.Pattern;

public class Utils {

    public static final HttpClient httpClient = HttpClient.newHttpClient();
    public static final Gson gson = new GsonBuilder().create();

    public static boolean patternMatches(String comparisonString, String regexPattern) {
        return Pattern.compile(regexPattern)
                .matcher(comparisonString)
                .matches();
    }

    /*
        Converts a string representation of a 2d array back to a 2d array
        Taken from: https://stackoverflow.com/a/22428926
     */
    public static char[][] stringToDeep(String str) {
        int row = 0;
        int col = 0;
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '[') {
                row++;
            }
        }
        row--;
        for (int i = 0;; i++) {
            if (str.charAt(i) == ',') {
                col++;
            }
            if (str.charAt(i) == ']') {
                break;
            }
        }
        col++;

        char[][] out = new char[row][col];

        str = str.replaceAll("\\[", "").replaceAll("\\]", "");

        String[] s1 = str.split(", ");

        int j = -1;
        for (int i = 0; i < s1.length; i++) {
            if (i % col == 0) {
                j++;
            }
            out[j][i % col] = (s1[i]).charAt(0);
        }
        return out;
    }
}