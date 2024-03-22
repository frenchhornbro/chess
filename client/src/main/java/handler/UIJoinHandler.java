package handler;

import ui.PrintHelper;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class UIJoinHandler extends UIHandler {

    /** Add a player to a game. Return false if any errors occur.  */
    public boolean join(String[] params, String authToken, ArrayList<Integer> gameIDs) {
        //params should be gameID and clientColor
        if (params.length != 2) {
            System.out.println("Incorrect number of parameters");
            PrintHelper.printJoin();
            return false;
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return false;
        }
        if (gameIDs == null || gameIDs.isEmpty()) {
            System.out.println("List games to see IDs");
            return false;
        }
        String gameID = params[0];
        String playerColor = params[1].toUpperCase();
        try {
            if (gameIDs.size() <= Integer.parseInt(gameID) || Integer.parseInt(gameID) < 0) {
                System.out.println("Invalid ID");
                return false;
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Invalid ID");
            return false;
        }
        if (!(playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
            System.out.println("Invalid Color");
            return false;
        }
        try {
            //Parameters
            String[] titles = {"gameID", "playerColor"};
            params[0] = convertID(gameID, gameIDs);
            params[1] = playerColor;

            //Prepare request
            HttpURLConnection http = prepareRequest("/game", "PUT",
                    "authorization", authToken, titles, params);

            //Process request
            try (InputStream response = http.getInputStream()) {
                return true;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            printError(ex.getMessage());
            return false;
        }
    }

    private String convertID(String displayID, ArrayList<Integer> storageIDs) {
        return storageIDs.get(Integer.parseInt(displayID)).toString();
    }
}
