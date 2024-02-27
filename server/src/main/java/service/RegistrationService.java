package service;
import model.UserData;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;

public class RegistrationService {
    private final MemoryUserDAO memUserDAO;
    private final MemoryAuthDAO memAuthDAO;

    public RegistrationService(MemoryUserDAO memUserDAO, MemoryAuthDAO memAuthDAO) {
        this.memUserDAO = memUserDAO;
        this.memAuthDAO = memAuthDAO;
    }

    public String register(String username, String password, String email) throws ServiceException { //Should return an authToken
        try {
            UserData user = getUser(username);
            if (user != null) throw new DataAccessException("Error: already taken");
            createUser(username, password, email);
            return createAuth(username);
        }
        catch (DataAccessException exception) {
            throw new ServiceException(exception.getMessage());
            //TODO: Populate the following errors:
            //
            //
            //
        }
    }

    private UserData getUser(String username) { //This is supposedly void, according to the initial diagram
        return this.memUserDAO.getUser(username);
    }

    private void createUser(String username, String password, String email) {
        this.memUserDAO.createUser(username, password, email);
    }

    private String createAuth(String username) {
        return this.memAuthDAO.createAuth(username);
    }

}
