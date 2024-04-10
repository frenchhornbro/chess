package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import dataStorage.GameStorage;
import dataStorage.GamesStorage;
import java.util.ArrayList;
import static chess.ChessBoard.BOARDSIZE;

public class SQLGameDAO extends SQLDAO {

    public SQLGameDAO() throws Exception {

    }

    public int createGame(String gameName) throws DataAccessException {
        /*
        GameData contains the following:
        int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game
        Not all of this has to be set, however.
        Users will eventually be set
        ChessGame and gameID should be set automatically
        Return the gameID
        */
        try {
            String createDataStatement = "INSERT INTO gameData (gameName) VALUES (?)";
            int gameID = updateDB(createDataStatement, gameName);

            ChessBoard chessBoard = new ChessBoard(true);
            ChessGame chessGame = new ChessGame(null, chessBoard);
            String createGameStatement = """
                INSERT INTO chessGame
                (gameID, teamTurn, stalemate, checkmate, gameOver,
                wKingRookMoved, wQueenRookMoved, wKingMoved,
                bKingRookMoved, bQueenRookMoved, bKingMoved)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
            updateDB(createGameStatement,
                gameID, chessGame.getTeamTurn(), chessGame.getStalemate(), chessGame.getCheckmate(), chessGame.getGameOver(),
                chessGame.getWKingRookMoved(), chessGame.getWQueenRookMoved(), chessGame.getWKingMoved(),
                chessGame.getBKingRookMoved(), chessGame.getBQueenRookMoved(), chessGame.getBKingMoved()
                );
            for (int i = 0; i < BOARDSIZE; i++) {
                for (int j = 0; j < BOARDSIZE; j++) {
                    ChessPiece thisPiece = chessBoard.getPiece(new ChessPosition(i+1, j+1));
                    ChessGame.TeamColor teamColor = (thisPiece == null) ? null : thisPiece.getTeamColor();
                    ChessPiece.PieceType pieceType = (thisPiece == null) ? null : thisPiece.getPieceType();
                    String createBoardStatement =
                            "INSERT INTO chessBoard (gameID, rowNum, colNum, playerColor, pieceType) VALUES (?, ?, ?, ?, ?)";
                    updateDB(createBoardStatement, gameID, i, j, teamColor, pieceType);
                }
            }

            return gameID;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public boolean gameIsNull (int gameID) throws DataAccessException {
        try {
            String queryStatement = "SELECT gameID FROM gameData where gameID=?";
            return (queryDB(queryStatement, gameID) == null);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public GamesStorage getGames() throws DataAccessException {
        //Get all the gameData, pack it into a GamesStorage Object, and return that
        try {
            String getGameIDsStatement = "SELECT gameID FROM gameData";
            ArrayList<String> gameIDs = queryArrayDB(getGameIDsStatement);

            ArrayList<GameStorage> games = new ArrayList<>();
            for (String gameID : gameIDs) {
                games.add(getGameData(gameID));
            }
            return new GamesStorage(games);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public GameStorage getGameData(String gameID) throws DataAccessException {
        try {
            //From gameData
            String getWhiteUserNameStatement = "SELECT whiteUsername FROM gameData WHERE gameID=?";
            String whiteUsername = queryDB(getWhiteUserNameStatement, gameID);
            String getBlackUserNameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";
            String blackUsername = queryDB(getBlackUserNameStatement, gameID);
            String getGameNameStatement = "SELECT gameName FROM gameData WHERE gameID=?";
            String gameName = queryDB(getGameNameStatement, gameID);
            ChessGame game = getGame(gameID);
            int intGameID = (int) Double.parseDouble(gameID);
            return new GameStorage(intGameID, whiteUsername, blackUsername, gameName, game);

        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public ChessGame getGame(String gameID) throws DataAccessException {
        try {
            //From chessGame
            String getTeamTurnStatement = "SELECT teamTurn FROM chessGame WHERE gameID=?";
            String teamTurnStr = queryDB(getTeamTurnStatement, gameID);
            ChessGame.TeamColor teamTurn = (teamTurnStr == null) ? null : switch (teamTurnStr.toUpperCase()) {
                case ("WHITE") -> ChessGame.TeamColor.WHITE;
                case ("BLACK") -> ChessGame.TeamColor.BLACK;
                default -> null;
            };
            String getStalemateStatement = "SELECT stalemate FROM chessGame WHERE gameID=?";
            boolean stalemate = (Integer.parseInt(queryDB(getStalemateStatement, gameID)) != 0);
            String getCheckmateStatement = "SELECT checkmate FROM chessGame WHERE gameID=?";
            boolean checkmate = (Integer.parseInt(queryDB(getCheckmateStatement, gameID)) != 0);
            String getGameOverStatement = "SELECT gameOver FROM chessGame WHERE gameID=?";
            boolean gameOver = (Integer.parseInt(queryDB(getGameOverStatement, gameID)) != 0);
            String getWKRMStatement = "SELECT wKingRookMoved FROM chessGame WHERE gameID=?";
            boolean wKingRookMoved = (Integer.parseInt(queryDB(getWKRMStatement, gameID)) != 0);
            String getWQRMStatement = "SELECT wQueenRookMoved FROM chessGame WHERE gameID=?";
            boolean wQueenRookMoved = (Integer.parseInt(queryDB(getWQRMStatement, gameID)) != 0);
            String getWKMStatement = "SELECT wKingMoved FROM chessGame WHERE gameID=?";
            boolean wKingMoved = (Integer.parseInt(queryDB(getWKMStatement, gameID)) != 0);
            String getBKRMStatement = "SELECT bKingRookMoved FROM chessGame WHERE gameID=?";
            boolean bKingRookMoved = (Integer.parseInt(queryDB(getBKRMStatement, gameID)) != 0);
            String getBQRMStatement = "SELECT bQueenRookMoved FROM chessGame WHERE gameID=?";
            boolean bQueenRookMoved = (Integer.parseInt(queryDB(getBQRMStatement, gameID)) != 0);
            String getBKMStatement = "SELECT bKingMoved FROM chessGame WHERE gameID=?";
            boolean bKingMoved = (Integer.parseInt(queryDB(getBKMStatement, gameID)) != 0);

            //From chessBoard
            ChessBoard board = getBoard((int) Double.parseDouble(gameID));
            return new ChessGame(teamTurn, board, stalemate, checkmate, gameOver, wKingRookMoved, wQueenRookMoved,
                                 wKingMoved, bKingRookMoved, bQueenRookMoved, bKingMoved);
        }
        catch (Exception ex) {
            System.out.print(ex.getMessage());
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void setGameValues(int gameID, String playerColor, String authToken) throws DataAccessException {
        //If playerColor is  WHITE or BLACK (not already taken), set them accordingly
        //If playerColor is null or some other String, set them as an observer
        try {
            String getUsernameStatement = "SELECT username FROM authData WHERE authToken=?";
            String username = queryDB(getUsernameStatement, authToken);
            if (playerColor == null) {
                String setObserverStatement = "INSERT INTO observers (gameID, username) VALUES (?, ?)";
                updateDB(setObserverStatement, gameID, username);
            }
            else if (playerColor.equals("WHITE")){
                String getWhiteUsernameStatement = "SELECT whiteUsername FROM gameData WHERE gameID=?";

                //Prevent user from joining if that color for that game is already taken
                // (unless they were the one that took it)
                String query = queryDB(getWhiteUsernameStatement, gameID);
                if (query != null && !query.equals(username)){
                    throw new DataAccessException("Error: already taken");
                }
                String setWhiteUsernameStatement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
                updateDB(setWhiteUsernameStatement, username, gameID);
            }
            else if (playerColor.equals("BLACK")){
                String getBlackUsernameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";

                //Prevent user from joining if that color for that game is already taken
                // (unless they were the one that took it)
                String query = queryDB(getBlackUsernameStatement, gameID);
                if (query != null && !query.equals(username)) {
                    throw new DataAccessException("Error: already taken");
                }
                String setBlackUsernameStatement = "UPDATE gameData SET blackUsername=? WHERE gameID=?";
                updateDB(setBlackUsernameStatement, username, gameID);
            }
            else {
                String setObserverStatement = "INSERT INTO observers (gameID, username) VALUES (?, ?)";
                updateDB(setObserverStatement, gameID, username);
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }

    }

    public ChessBoard getBoard(int gameID) throws Exception {
        String playerColorStatement = "SELECT playerColor from chessBoard WHERE gameID=? AND rowNum=? AND colNum=?";
        String pieceTypeStatement = "SELECT pieceType from chessBoard WHERE gameID=? AND rowNum=? AND colNum=?";
        ChessPiece[][] squares = new ChessPiece[BOARDSIZE][BOARDSIZE];
        for (int i = 0; i < BOARDSIZE; i++) {
            for (int j = 0; j < BOARDSIZE; j++) {
                String playerColorStr = queryDB(playerColorStatement, gameID, i, j);
                String pieceTypeStr = queryDB(pieceTypeStatement, gameID, i, j);
                if (playerColorStr != null && pieceTypeStr != null) {
                    ChessGame.TeamColor playerColor =
                            (playerColorStr.equals("WHITE")) ? ChessGame.TeamColor.WHITE: ChessGame.TeamColor.BLACK;
                    ChessPiece.PieceType pieceType = switch (pieceTypeStr) {
                        case "KING" -> ChessPiece.PieceType.KING;
                        case "QUEEN" -> ChessPiece.PieceType.QUEEN;
                        case "ROOK" -> ChessPiece.PieceType.ROOK;
                        case "BISHOP" -> ChessPiece.PieceType.BISHOP;
                        case "KNIGHT" -> ChessPiece.PieceType.KNIGHT;
                        default -> ChessPiece.PieceType.PAWN;
                    };
                    squares[i][j] = new ChessPiece(playerColor, pieceType);
                }
            }
        }
        return new ChessBoard(squares);
    }

    public void updateGame(String gameID, ChessGame game) throws Exception {
        String updateGameStatement = """
            UPDATE chessGame
            SET teamTurn=?, stalemate=?, checkmate=?, gameOver=?,
            wKingRookMoved=?, wQueenRookMoved=?, wKingMoved=?,
            bKingRookMoved=?, bQueenRookMoved=?, bKingMoved=?
            WHERE gameID=?
            """;
        updateDB(updateGameStatement,
                game.getTeamTurn(), game.getStalemate(), game.getCheckmate(), game.getGameOver(),
                game.getWKingRookMoved(), game.getWQueenRookMoved(), game.getWKingMoved(),
                game.getBKingRookMoved(), game.getBQueenRookMoved(), game.getBKingMoved(),
                gameID
        );
    }

    public void clear() throws Exception {
        String clearStatement = "DELETE FROM gameData";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessGame";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessBoard";
        updateDB(clearStatement);
    }
}
