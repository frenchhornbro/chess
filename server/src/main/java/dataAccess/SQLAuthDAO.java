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
            //TODO: Check if the Register Service is doing its codes the right way
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

    //Delete an authToken
    public void deleteAuth(String authToken) throws DataAccessException {
        try {
            String deleteStatement = "DELETE FROM authData WHERE authToken=?";
            updateDB(deleteStatement, authToken);
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