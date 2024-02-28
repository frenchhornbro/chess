package dataAccess;
import model.AuthData;

import java.util.HashMap;
import java.util.UUID;

public class MemoryAuthDAO {
    private final HashMap<String, AuthData> authDatabase = new HashMap<>();
    public MemoryAuthDAO() {

    }

    public AuthData createAuth(String username) { //Return an authToken
        String authToken = UUID.randomUUID().toString();
        AuthData newAuth = new AuthData(authToken, username);
        authDatabase.put(authToken, newAuth);
        return newAuth;
    }

    public AuthData getAuth(String authToken) {
        return authDatabase.get(authToken);
    }

    public void deleteAuth(AuthData authData) {
        authDatabase.remove(authData.getAuthToken());
    }

    public void clear() {
        //TODO: Check if this memory is adequately deallocated
        authDatabase.clear();
    }
}
