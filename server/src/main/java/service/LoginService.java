package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

public class LoginService {
    private final SQLUserDAO memUserDao;
    private final SQLAuthDAO memAuthDao;
    public LoginService() throws Exception {
        this.memUserDao = new SQLUserDAO();
        this.memAuthDao = new SQLAuthDAO();
    }

    //If username and password are correct, then return an authToken
    public String login(String username, String password) throws ServiceException {
        try {
            String pwd = this.memUserDao.getUser(username);
            //TODO: If the user is null or not in the DB, a SQLException or DataAccessException would be thrown, right?
            if (!pwd.equals(password)) throw new ServiceException("Error: password is incorrect", 401);
            return this.memAuthDao.createAuth(username);
        }
        catch (DataAccessException ex) {
            //TODO: Make sure 401 is the correct error here
            throw new ServiceException(ex.getMessage(), 401);
        }
    }
}
