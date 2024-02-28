package dataAccess;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();
    public MemoryAuthDAO() {

    }

    public String createAuth(String username) { //Return an authToken
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDatabase.put(username, newAuth);
        return authToken;
    }

    public AuthData getAuth() {
        throw new RuntimeException("Not yet implemented");
    }

    public void deleteAuth() {
        throw new RuntimeException("Not yet implemented");
    }

    public void clear() {
        //TODO: Check if this memory is adequately deallocated
        authDatabase.clear();
    }
}
