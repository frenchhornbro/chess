package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import model.AuthData;

public class LoginService {
    private final MemoryUserDAO memUserDao;
    private final MemoryAuthDAO memAuthDao;
    public LoginService(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao) {
        this.memUserDao = memUserDao;
        this.memAuthDao = memAuthDao;
    }

    public AuthData login(String username, String password) throws ServiceException {
        if (this.memUserDao.getUser(username) != null) {
            //TODO: Verify the password
            return this.memAuthDao.createAuth(username);
        }
        else throw new ServiceException("Error: user does not exist", "401");
        /*
        TODO: Populate the following errors:
         401 - Error: unauthorized
         500 - Error: description
         */
    }
}
