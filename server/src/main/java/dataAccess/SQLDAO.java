package dataAccess;

import chess.ChessGame;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import static java.sql.Types.NULL;

public class SQLDAO {
    public SQLDAO() throws Exception {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    //Create tables if they don't exist
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData(
                username VARCHAR(256) NOT NULL PRIMARY KEY,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS authData(
                authToken VARCHAR(36) NOT NULL PRIMARY KEY,
                username VARCHAR(256) NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS gameData(
                gameID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
                gameName VARCHAR(128) NOT NULL,
                whiteUsername VARCHAR(256),
                blackUsername VARCHAR(256)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS observers(
                gameID INT NOT NULL,
                username VARCHAR(256)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS chessGame(
                gameID INTEGER NOT NULL PRIMARY KEY,
                teamTurn CHAR(5),
                stalemate TINYINT NOT NULL,
                checkmate TINYINT NOT NULL,
                wKingRookMoved TINYINT NOT NULL,
                wQueenRookMoved TINYINT NOT NULL,
                wKingMoved TINYINT NOT NULL,
                bKingRookMoved TINYINT NOT NULL,
                bQueenRookMoved TINYINT NOT NULL,
                bKingMoved TINYINT NOT NULL
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS chessBoard(
                gameID INTEGER NOT NULL,
                rowNum INTEGER NOT NULL,
                colNum INTEGER NOT NULL,
                playerColor CHAR(5),
                pieceType CHAR(6)
            );
            """
    };

    //Initialize the database configuration
    protected void configureDatabase() throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement: createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
    }

    //Query the DB for one element
    protected String queryDB(String statement, Object... params) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepState = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> prepState.setString(i + 1, p);
                        case Integer p -> prepState.setInt(i + 1, p);
                        case ChessGame p -> prepState.setString(i + 1, p.toString());
                        case null, default -> prepState.setNull(i + 1, NULL);
                    }
                }
                try (var response = prepState.executeQuery()) {
                    if (response.next()) {
                        return response.getString(1); //return the String
                    }
                }
            }
        }
        return null;
    }

    //Queries the DB for multiple elements
    protected ArrayList<String> queryArrayDB(String statement, Object... params) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepState = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> prepState.setString(i + 1, p);
                        case Integer p -> prepState.setInt(i + 1, p);
                        case ChessGame p -> prepState.setString(i + 1, p.toString());
                        case null, default -> prepState.setNull(i + 1, NULL);
                    }
                }
                try (var response = prepState.executeQuery()) {
                    ArrayList<String> retList = new ArrayList<>();
                    while (response.next()) {
                        retList.add(response.getString(1)); //append to the array
                    }
                    return retList;
                }
            }
        }
    }

        //Send updates to the SQL Database
    protected int updateDB(String statement, Object... params) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepState = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> prepState.setString(i + 1, p);
                        case Integer p -> prepState.setInt(i + 1, p);
                        case ChessGame p -> prepState.setString(i + 1, p.toString());
                        case null, default -> prepState.setNull(i + 1, NULL);
                    }
                }
                prepState.executeUpdate();

                var retState = prepState.getGeneratedKeys();
                if (retState.next()) return retState.getInt(1);
                return 0;
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}