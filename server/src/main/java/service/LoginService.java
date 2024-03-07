package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLUserDAO;
import model.AuthData;

public class LoginService {
    private final SQLUserDAO memUserDao;
    private final SQLAuthDAO memAuthDao;
    public LoginService(SQLUserDAO memUserDao, SQLAuthDAO memAuthDao) {
        this.memUserDao = memUserDao;
        this.memAuthDao = memAuthDao;
    }

    public AuthData login(String username, String password) throws ServiceException {
        //Check that user exists in database and password is correct and if so return authData
        if (this.memUserDao.getUser(username) != null) {
            if (this.memUserDao.getUser(username).getPwd().equals(password)) {
                return this.memAuthDao.createAuth(username);
            }
            else throw new ServiceException("Error: password is incorrect", 401);
        }
        else throw new ServiceException("Error: user does not exist", 401);
    }
}
