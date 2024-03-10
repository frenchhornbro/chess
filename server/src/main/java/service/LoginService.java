package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;

public class LoginService {
    private final SQLUserDAO sqlUserDao;
    private final SQLAuthDAO sqlAuthDao;
    public LoginService() throws Exception {
        this.sqlUserDao = new SQLUserDAO();
        this.sqlAuthDao = new SQLAuthDAO();
    }

    //If username and password are correct, return an authToken
    public String login(String username, String password) throws ServiceException {
        try {
            String pwd = this.sqlUserDao.getUser(username);
            if (pwd == null) {
                throw new ServiceException("Error: user does not exist", 401);
            }
            if (!pwd.equals(password)) {
                throw new ServiceException("Error: password is incorrect", 401);
            }
            //TODO: Consider no longer deleting old auths and instead storing them in a separate table
            this.sqlAuthDao.deleteAuth(username, false);
            return this.sqlAuthDao.createAuth(username);
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 401);
        }
    }
}
