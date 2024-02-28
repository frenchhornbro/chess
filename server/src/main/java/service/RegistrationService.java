package service;
import model.AuthData;
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

    public AuthData register(String username, String password, String email) throws ServiceException { //Should return an authToken
        try {
            UserData user = this.memUserDAO.getUser(username);
            if (user != null) throw new DataAccessException("Error: already taken");
            this.memUserDAO.createUser(username, password, email);
            return this.memAuthDAO.createAuth(username);
        }
        catch (DataAccessException exception) {
            throw new ServiceException(exception.getMessage());
            /*
            TODO: Populate the following errors:
             400 - Error: bad request
             500 - Error: description
             403 - Error: already taken
            */
        }
    }
}
