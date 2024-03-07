package service;

import dataAccess.SQLAuthDAO;
import model.AuthData;

public class LogoutService {
    private final SQLAuthDAO memAuthDao;

    public LogoutService(SQLAuthDAO memAuthDao) {
        this.memAuthDao = memAuthDao;
    }

    public void logout(String authToken) throws ServiceException {
        //Check if authToken is correct, and if so log out
        AuthData authData = this.memAuthDao.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        this.memAuthDao.deleteAuth(authData);
    }
}
