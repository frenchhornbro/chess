package dataAccess;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;
import java.sql.*;

public class SQLAuthDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();
    //TODO: ^^^ Get rid of this
    public SQLAuthDAO() {
        //TODO: Figure out how to do this from pet shop
        String connectionURL = "jdbc:sqlserver://localhost:3306?user=root&password=USw9SNs!kp%cei";
        Connection connection = null;
        // Are we supposed to get this ^^^ in Database Manager...

        //TODO: Perhaps create the databases in code rather than manually?
    }

    public AuthData createAuth(String username) {
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDatabase.put(authToken, newAuth);
        //TODO: Replace this with a SQL command: insert into authData("username", "authToken");
        return newAuth;
    }

    public AuthData getAuth(String authToken) {
        return authDatabase.get(authToken);
    }

    public void deleteAuth(AuthData authData) {
        authDatabase.remove(authData.getAuthToken());
    }

    public void clear() {
        authDatabase.clear();
    }
}
