package dataAccess;

import chess.ChessGame;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class SQLDAO {
    public SQLDAO() throws Exception {
        DatabaseManager.createDatabase();
        configureDatabase();
    }

    //Create tables if they don't exist
    private final String[] createStatements = {//TODO: Consider doing BEGIN TRANSACTION and COMMIT TRANSACTION
            """
            CREATE TABLE IF NOT EXISTS userData(
                id INTEGER NOT NULL AUTO_INCREMENT,
                username VARCHAR(256) NOT NULL,
                password VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL,
                UNIQUE(username),
                PRIMARY KEY(id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS authData(
                id INTEGER NOT NULL AUTO_INCREMENT,
                username VARCHAR(256) NOT NULL,
                authToken VARCHAR(36) NOT NULL,
                UNIQUE(username),
                PRIMARY KEY(id)
            );
            """,
            """
            CREATE TABLE IF NOT EXISTS gameData(
                id INTEGER NOT NULL AUTO_INCREMENT,
                gameID INT NOT NULL,
                gameName VARCHAR(128) NOT NULL,
                whiteUsername VARCHAR(256),
                blackUsername VARCHAR(256),
                chessGame TEXT NOT NULL,
                PRIMARY KEY(id)
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

    //Send updates to the SQL Database
    protected int updateDB(String statement, Object... params) throws Exception {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepState = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) prepState.setString(i+1, p);
                    else if (param instanceof Integer p) prepState.setInt(i+1, p);
                    else if (param instanceof ChessGame p) prepState.setString(i+1, p.toString());
                    //TODO Check if toString() is correct here ^^^
                    else prepState.setNull(i+1, NULL);
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