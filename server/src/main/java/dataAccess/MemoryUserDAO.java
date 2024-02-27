package dataAccess;
import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO {
    private final HashMap<String, UserData> userDatabase = new HashMap<>();
    public MemoryUserDAO() {

    }

    public void clear() {
        userDatabase.clear();
    }

    public void createUser(String username, String pwd, String email) {
        UserData newUser = new UserData(username, pwd, email);
        userDatabase.put(username, newUser);
    }

    public UserData getUser(String username) {
        return userDatabase.get(username);
    }
}
