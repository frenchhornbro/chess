package ui;

public class PrintHelper {

    public static void printPreLoginUI() {
        System.out.println("\t\033[36;107;1m[Logged Out UI]\033[39;49;0m");
    }

    public static void printPostLoginUI() {
        System.out.println("\t\033[36;107;1m[Logged In UI]\033[39;49;0m");
    }

    public static void printRegister() {
        System.out.println("\033[32mregister\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m \033[31m<EMAIL>\033[39m - create account");
    }

    public static void printLogin() {
        System.out.println("\033[32mlogin\033[39m \033[31m<USERNAME>\033[39m \033[31m<PASSWORD>\033[39m - login");
    }

    public static void printQuit() {
        System.out.println("\033[32mquit\033[39m - exit application");
    }

    public static void printHelp() {
        System.out.println("\033[32mhelp\033[39m - display possible commands");
    }

    public static void printClear() {
        System.out.println("\033[32mclear\033[39m - clear the screen");
    }

    public static void printCreate() {
        System.out.println("\033[32mcreate\033[39m \033[31m<GAMENAME>\033[39m - create a new game");
    }

    public static void printList() {
        System.out.println("\033[32mlist\033[39m - list all games");
    }

    public static void printJoin() {
        System.out.println("\033[32mjoin\033[39m \033[31m<GAMEID>\033[39m \033[31m<COLOR>\033[39m - join a game as " +
                "\033[30;107mWHITE\033[39;49m or \033[40;37mBLACK\033[39;49m");
    }

    public static void printObserve() {
        System.out.println("\033[32mobserve\033[39m \033[31m<GAMEID>\033[39m - join a game as an observer");
    }

    public static void printLogout() {
        System.out.println("\033[32mlogout\033[39m - display possible commands");
    }

    public static void printDraw() {
        System.out.println("\033[32mdraw\033[39m - redraw the chess board");
    }

    public static void printHighlight() {
        System.out.println("\033[32mhighlight\033[39m \033[31m<COORDINATES>\033[39m - highlight legal moves for a piece");
    }

    public static void printMove() {
        System.out.println("\033[32mmove\033[39m \033[31m<START COORDINATE> <END COORDINATE>\033[39m - make a move");
    }

    public static void printResign() {
        System.out.println("\033[32mresign\033[39m - concede the victory to your opponent");
    }

    public static void printLeave() {
        System.out.println("\033[32mleave\033[39m - leave the game");
    }
}
