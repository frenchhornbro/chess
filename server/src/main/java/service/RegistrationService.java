package service;
import model.AuthData;
import model.UserData;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

public class RegistrationService {
    private final SQLUserDAO memUserDAO;
    private final SQLAuthDAO memAuthDAO;

    public RegistrationService(SQLUserDAO memUserDAO, SQLAuthDAO memAuthDAO) {
        this.memUserDAO = memUserDAO;
        this.memAuthDAO = memAuthDAO;
    }

    public AuthData register(String username, String password, String email) throws ServiceException { //Should return an authToken
        //Check that username, pwd, and email are valid inputs and that the username is not already taken
        //If these conditions are met, return authData
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
            int errorCode = (exception.getMessage().equals("Error: bad request")) ? 400 : 403;
            throw new ServiceException(exception.getMessage(), errorCode);
        }
    }
}
