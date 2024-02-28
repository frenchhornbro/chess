package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {
    private final MemoryUserDAO memUserDAO;
    private final MemoryAuthDAO memAuthDAO;

    public ClearService(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao) {
        this.memUserDAO = memUserDao;
        this.memAuthDAO = memAuthDao;
    }

    public void clear() {
        this.memUserDAO.clear();
        this.memAuthDAO.clear();
    }
}
