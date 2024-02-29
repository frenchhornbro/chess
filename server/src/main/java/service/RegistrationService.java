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
            if (username == null || password == null || email == null)
                throw new DataAccessException("Error: bad request");
            if (username.isEmpty() || password.isEmpty() || email.isEmpty())
                throw new DataAccessException("Error: bad request");
            UserData user = this.memUserDAO.getUser(username);
            if (user != null) throw new DataAccessException("Error: already taken");
            this.memUserDAO.createUser(username, password, email);
            return this.memAuthDAO.createAuth(username);
        }
        catch (DataAccessException exception) {
            int errorCode;
            if (exception.getMessage().equals("Error: bad request")) errorCode = 400;
            else errorCode = 403;
            throw new ServiceException(exception.getMessage(), errorCode);
        }
    }
}
