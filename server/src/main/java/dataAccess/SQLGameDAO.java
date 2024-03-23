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
                (gameID, teamTurn, stalemate, checkmate,
                wKingRookMoved, wQueenRookMoved, wKingMoved,
                bKingRookMoved, bQueenRookMoved, bKingMoved)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);
                """;
            updateDB(createGameStatement,
                gameID, chessGame.getTeamTurn(), chessGame.getStalemate(), chessGame.getCheckmate(),
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
                String getWhiteUserNameStatement = "SELECT whiteUsername FROM gameData WHERE gameID=?";
                String whiteUsername = queryDB(getWhiteUserNameStatement, gameID);
                String getBlackUserNameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";
                String blackUsername = queryDB(getBlackUserNameStatement, gameID);
                String getGameNameStatement = "SELECT gameName FROM gameData WHERE gameID=?";
                String gameName = queryDB(getGameNameStatement, gameID);
                GameStorage gameStorage = new GameStorage(Integer.parseInt(gameID), whiteUsername, blackUsername, gameName);
                games.add(gameStorage);
            }
            return new GamesStorage(games);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void updateGame(int gameID, String playerColor, String authToken) throws DataAccessException {
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
                if (queryDB(getWhiteUsernameStatement, gameID) != null){
                    throw new DataAccessException("Error: already taken");
                }
                String setWhiteUsernameStatement = "UPDATE gameData SET whiteUsername=? WHERE gameID=?";
                updateDB(setWhiteUsernameStatement, username, gameID);
            }
            else if (playerColor.equals("BLACK")){
                String getBlackUsernameStatement = "SELECT blackUsername FROM gameData WHERE gameID=?";
                if (queryDB(getBlackUsernameStatement, gameID) != null) {
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

    public void clear() throws Exception {
        String clearStatement = "DELETE FROM gameData";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessGame";
        updateDB(clearStatement);
        clearStatement = "DELETE FROM chessBoard";
        updateDB(clearStatement);
    }
}
