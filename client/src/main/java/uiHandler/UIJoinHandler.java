package uiHandler;

import ui.PrintHelper;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class UIJoinHandler extends UIHandler {

    /** Add a player to a game, or add a player as an observer. Return false if any errors occur.  */
    public String join(int port, ArrayList<String> params, String authToken, ArrayList<Integer> gameIDs,
                       boolean observe, String username) {
        //params should be gameID and clientColor
        if ((params.size() != 1 && observe) || (params.size() != 2 && !observe)) {
            System.out.println("Incorrect number of parameters");
            if (observe) PrintHelper.printObserve();
            else PrintHelper.printJoin();
            return "false";
        }
        if (authToken == null) {
            System.out.println("No auth token");
            return "false";
        }
        if (gameIDs == null) {
            System.out.println("List games to see IDs");
            return "false";
        }
        if (gameIDs.isEmpty()) {
            System.out.println("No games created (call list to update created games)");
            return "false";
        }
        String gameID = params.getFirst();
        String playerColor = (observe) ? null : params.get(1).toUpperCase();
        try {
            if (gameIDs.size() <= Integer.parseInt(gameID) || Integer.parseInt(gameID) < 0) {
                System.out.println("Invalid ID");
                return "false";
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Invalid ID");
            return "false";
        }
        if (!observe) {
            if (!(playerColor.equals("WHITE") || playerColor.equals("BLACK"))) {
                System.out.println("Invalid Color");
                return "false";
            }
        }
        try {
            //Parameters and Headers
            ArrayList<String> titles = new ArrayList<>();
            titles.add("gameID");
            ArrayList<Object> joinParams = new ArrayList<>(List.of(Double.valueOf(convertID(gameID, gameIDs))));
            if (!observe) {
                titles.add("playerColor");
                joinParams.add(playerColor);
                titles.add("username");
                joinParams.add(username);
            }

            //Prepare request
            HttpURLConnection http = prepareRequest("/game", port, "PUT",
                    "authorization", authToken, titles, joinParams);

            //Process request
            try (InputStream response = http.getInputStream()) {
                return gameID;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            printError(ex.getMessage());
            return "false";
        }
    }

    /** Takes a display ID and the ArrayList for storageIDs and returns the storageID as an int */
    public static int convertID(String displayID, ArrayList<Integer> storageIDs) {
        return storageIDs.get(Integer.parseInt(displayID));
    }
}
