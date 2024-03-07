package dataAccess;

import chess.ChessGame;

import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class SQLDAO {
    public SQLDAO() throws Exception {
        configureDatabase();
    }

    //Create tables if they don't exist
    private final String[] createStatements = {
            """
            create database if not exists chess;
            use chess;
            create table if not exists userData(
            id integer not null auto_increment,
            username varchar(256) not null,
            password varchar(256) not null,
            email varchar(256) not null,
            primary key(id)
            );
            create table if not exists authData(
            id integer not null auto_increment,
            username varchar(128) not null,
            password varchar(128) not null,
            email varchar(128) not null,
            primary key(id)
            );
            create table if not exists gameData(
            id integer not null auto_increment,
            gameID int not null,
            gameName varChar(128) not null,
            whiteUsername varchar(256),
            blackUsername varchar(256),
            chessGame text
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