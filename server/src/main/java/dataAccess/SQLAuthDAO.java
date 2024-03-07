package dataAccess;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;
import java.sql.*;

import static java.sql.Types.NULL;

public class SQLAuthDAO extends SQLDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();
    //TODO: ^^^ Get rid of this
    public SQLAuthDAO() throws Exception {

    }

    //Create an authToken and return it
    public String createAuth(String username) throws DataAccessException {
        try {
            String authToken = UUID.randomUUID().toString();
            String createStatement = "insert into authData (username, authToken) values (?, ?);";
            int id = updateDB(createStatement, username, authToken);
            return authToken;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
            //TODO: Check if the Register Service is doing its codes the right way
        }
    }

    //Return an authToken if it exists
    public String getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "select authToken from authData where authToken = ?";
            try (var prepState = conn.prepareStatement(getStatement)) {
                prepState.setString(1, authToken);
                try (var response = prepState.executeQuery()) {
                    if (response.next()) {
                        return response.getString(1); //return the authToken
                    }
                }
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }

    //Delete an authToken
    public void deleteAuth(AuthData authData) {
        authDatabase.remove(authData.getAuthToken());
    }

    //Clear all authTokens
    public void clear() {
        authDatabase.clear();
    }
}
