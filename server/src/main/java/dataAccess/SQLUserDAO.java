package dataAccess;
import model.UserData;
import java.util.HashMap;

public class SQLUserDAO extends SQLDAO {
    private final HashMap<String, UserData> userDatabase = new HashMap<>();
    public SQLUserDAO() throws Exception {

    }

    public void clear() {
        userDatabase.clear();
    }

    //Create a user and return the ID
    public int createUser(String username, String password, String email) throws DataAccessException {
        UserData newUser = new UserData(username, password, email);
        try {
            String createStatement = "insert into userdata (username, password, email) values (?, ?, ?);";
            return updateDB(createStatement, username, password, email);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Return the encoded password of the user in the database
    //TODO: Add this to SQLDAO as getData (they're basically identical)
    public String getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String getStatement = "select password from userData where username=?";
            try (var prepState = conn.prepareStatement(getStatement)) {
                prepState.setString(1, username);
                try (var response = prepState.executeQuery()) {
                    if (response.next()) {
                        return response.getString(1); //return the password
                    }
                }
            }
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
        return null;
    }
}
