package dataAccess;

public class SQLUserDAO extends SQLDAO {
    public SQLUserDAO() throws Exception {

    }

    public void clear() throws  Exception {
        String clearStatement = "DELETE FROM userData";
        updateDB(clearStatement);
    }

    //Create a user and return the ID
    public void createUser(String username, String password, String email) throws DataAccessException {
        try {
            String createStatement = "insert into userData (username, password, email) values (?, ?, ?);";
            updateDB(createStatement, username, password, email);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    //Return the encoded password of the user in the database
    public String getUser(String username) throws DataAccessException {
        try {
            String getStatement = "select password from userData where username=?";
            return queryDB(getStatement, username);
        }
        catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
}
