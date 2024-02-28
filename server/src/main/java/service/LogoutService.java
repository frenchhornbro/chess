package service;

import dataAccess.MemoryAuthDAO;
import model.AuthData;

public class LogoutService {
    private final MemoryAuthDAO memAuthDao;

    public LogoutService(MemoryAuthDAO memAuthDao) {
        this.memAuthDao = memAuthDao;
    }

    public void logout(String authToken) throws ServiceException {
        AuthData authData = this.memAuthDao.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        this.memAuthDao.deleteAuth(authData);
    }
}
