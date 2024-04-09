package dataAccess;
import java.util.UUID;

public class SQLAuthDAO extends SQLDAO {
    public SQLAuthDAO() throws Exception {

    }

    //Create an authToken and return it
    public String createAuth(String username) throws DataAccessException {
        try {
            String authToken = UUID.randomUUID().toString();
            String createStatement = "insert into authData (username, authToken) values (?, ?);";
            updateDB(createStatement, username, authToken);
            return authToken;
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Return an authToken if it exists
    public String getAuth(String authToken) throws DataAccessException {
        try {
            String getStatement = "select authToken from authData where authToken = ?";
            return queryDB(getStatement, authToken);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public String getUser(String authToken) throws DataAccessException {
        try {
            String getStatement = "select username from authData where authToken=?";
            return queryDB(getStatement, authToken);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Delete an authToken
    public void deleteAuth(String authOrUser, boolean isAuth) throws DataAccessException {
        try {
            String deleteStatement = "DELETE FROM authData WHERE";
            if (isAuth) deleteStatement += " authToken=?";
            else deleteStatement += " username=?";
            updateDB(deleteStatement, authOrUser);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Clear all authTokens
    public void clear() throws Exception {
        String clearStatement = "DELETE FROM authData";
        updateDB(clearStatement);
    }
}