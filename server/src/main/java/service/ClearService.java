package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;

public class ClearService {
    private final MemoryUserDAO memUserDAO;
    private final MemoryAuthDAO memAuthDAO;
    private final MemoryGameDAO memGameDAO;

    public ClearService(MemoryUserDAO memUserDao, MemoryAuthDAO memAuthDao, MemoryGameDAO memGameDao) {
        this.memUserDAO = memUserDao;
        this.memAuthDAO = memAuthDao;
        this.memGameDAO = memGameDao;
    }

    public void clear() {
        this.memUserDAO.clear();
        this.memAuthDAO.clear();
        this.memGameDAO.clear();
    }
}
