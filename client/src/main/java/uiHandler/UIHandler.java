package uiHandler;

import chess.ChessPiece;
import com.google.gson.Gson;
import ui.EscapeSequences;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import static chess.ChessPiece.PieceType.*;

public class UIHandler {
    public void clearScreen() {
        System.out.print(EscapeSequences.ERASE_SCREEN);
    }

    public boolean clearData(int port) {
        try {
            HttpURLConnection http = prepareRequest("/db", port, "DELETE", null, null,
                                                null, null);
            try (InputStream response = http.getInputStream()) {
                return true;
            }
        }
        catch (Exception ex) {
            printError(ex.getMessage());
            return false;
        }
    }

    protected <T> HttpURLConnection prepareRequest(String slug, int port, String requestMethod, String headerName, String headerValue,
                                            ArrayList<String> fields, ArrayList<T> params) throws Exception {
        URI uri = new URI("http://localhost:" + port + slug);
        URL url = uri.toURL();
        HttpURLConnection http = (HttpURLConnection) url.openConnection();

        http.setConnectTimeout(5000);
        http.setRequestMethod(requestMethod);
        http.setDoInput(true);
        http.setDoOutput(true);

        //Header
        if (headerName != null) http.addRequestProperty(headerName, headerValue);

        //Body
        if (fields != null && params != null && !fields.isEmpty() && !params.isEmpty()) {
            if (fields.size() != params.size()) throw new Exception("Error: Fields and params were of different length");
            HashMap<String, T> bodyVars = new HashMap<>();
            for (int i = 0; i < fields.size(); i++) {
                bodyVars.put(fields.get(i), params.get(i));
            }
            try (OutputStream outputStream = http.getOutputStream()) {
                var jsonBody = new Gson().toJson(bodyVars);
                outputStream.write(jsonBody.getBytes());
            }
        }

        http.connect();
        return http;
    }

    /** Convert a letter in a Chess coordinate for use in a ChessPosition object
     * <p>
     * (int) 'a' = 97
     * */
    public static int convertChar(char letter) {
        return (int) letter - 96;
    }

    /** Convert a number in a Chess coordinate for use in a ChessPosition object
     * <p>
     * (int) '1' = 49
     * */
    public static int convertNum(char num) {
        return (int) num - 48;
    }

    public static boolean validCoordinate(String coordinate) {
        if (coordinate.length() != 2) {
            System.out.println("Incorrect coordinate: " + coordinate);
            return false;
        }

        // Convert params to ChessPosition: (int) a = 97, (int) 1 = 49
        int letter = convertChar(coordinate.charAt(0));
        int num = convertNum(coordinate.charAt(1));
        if (letter < 1 || letter > 8 || num < 1 || num > 8) {
            System.out.println("Incorrect coordinate: " + coordinate);
            return false;
        }
        return true;
    }

    public static ChessPiece.PieceType toPieceType(String pieceType) {
        return switch (pieceType.toUpperCase()) {
            case "KING" -> KING;
            case "QUEEN" -> QUEEN;
            case "ROOK" -> ROOK;
            case "BISHOP" -> BISHOP;
            case "KNIGHT" -> KNIGHT;
            default -> PAWN;
        };
    }

    public static boolean validPromotionType(String pieceType) {
        if (pieceType == null) return false;
        return (pieceType.equalsIgnoreCase("QUEEN") || pieceType.equalsIgnoreCase("ROOK")
                || pieceType.equalsIgnoreCase("BISHOP") || pieceType.equalsIgnoreCase("KNIGHT"));
    }

    protected void printError(String errorMsg) {
        String printStr = "Error";
        String testStr = "Server returned HTTP response code: ";
        String[] errorCodes = {"400", "401", "403", "500"};
        int errorNum = 200;
        for (String errorCode : errorCodes) {
            if (errorMsg.contains(testStr + errorCode)) {
                errorNum = Integer.parseInt(errorCode);
                break;
            }
        }

        switch (errorNum) {
            case (400):
                printStr += " 400: bad request";
                break;
            case (401):
                printStr += " 401: unauthorized";
                break;
            case (403):
                printStr += " 403: already taken";
                break;
            case (500):
                printStr += " 500: system error";
                break;
            default:
                printStr += ": Server not started";
        }
        System.out.println(printStr);
    }
}