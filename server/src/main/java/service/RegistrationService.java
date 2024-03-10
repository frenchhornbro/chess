package service;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

public class RegistrationService {
    private final SQLUserDAO sqlUserDAO;
    private final SQLAuthDAO sqlAuthDAO;

    public RegistrationService() throws Exception {
        this.sqlUserDAO = new SQLUserDAO();
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    public String register(String username, String password, String email) throws ServiceException { //Should return an authToken
        //Check that username, pwd, and email are valid inputs and that the username is not already taken
        //If these conditions are met, return authToken
        try {
            if (username == null || password == null || email == null)
                throw new ServiceException("Error: bad request", 400);
            if (username.isEmpty() || password.isEmpty() || email.isEmpty())
                throw new ServiceException("Error: bad request", 400);
            String user = this.sqlUserDAO.getUser(username);
            if (user != null) throw new DataAccessException("Error: already taken");

            this.sqlUserDAO.createUser(username, password, email);
            return this.sqlAuthDAO.createAuth(username);
        }
        catch (DataAccessException exception) {
            throw new ServiceException(exception.getMessage(), 403);
        }
    }
}
