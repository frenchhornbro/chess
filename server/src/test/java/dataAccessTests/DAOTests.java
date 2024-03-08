package dataAccessTests;

import chess.ChessGame;
import dataAccess.DataAccessException;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static java.sql.Types.NULL;

public class DAOTests {
    protected String performQuery(String queryStatement, Object... params) throws Exception {
        Class<?> clazz = Class.forName("dataAccess.DatabaseManager");
        Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);

        Object obj = clazz.getDeclaredConstructor().newInstance();
        try (Connection conn = (Connection) getConnectionMethod.invoke(obj)) {
            try (var prepState = conn.prepareStatement(queryStatement)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) prepState.setString(i+1, p);
                    else prepState.setNull(i+1, NULL);
                }
                try (var response = prepState.executeQuery()) {
                    if (response.next()) {
                        return response.getString(1); //return the String
                    }
                }
            }
        }
        return "Not found";
    }

    protected int performUpdate(String statement, Object... params) throws Exception {
        Class<?> clazz = Class.forName("dataAccess.DatabaseManager");
        Method getConnectionMethod = clazz.getDeclaredMethod("getConnection");
        getConnectionMethod.setAccessible(true);

        Object obj = clazz.getDeclaredConstructor().newInstance();
        try (Connection conn = (Connection) getConnectionMethod.invoke(obj)) {
            try (var prepState = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) prepState.setString(i+1, p);
                    else if (param instanceof Integer p) prepState.setInt(i+1, p);
                    else if (param instanceof ChessGame p) prepState.setString(i+1, p.toString());
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
