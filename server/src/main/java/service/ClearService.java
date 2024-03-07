package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;

public class ClearService {
    private final SQLUserDAO memUserDAO;
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public ClearService(SQLUserDAO memUserDao, SQLAuthDAO memAuthDao, SQLGameDAO memGameDao) {
        this.memUserDAO = memUserDao;
        this.memAuthDAO = memAuthDao;
        this.memGameDAO = memGameDao;
    }

    public void clear() {
        //Clear all data
        this.memUserDAO.clear();
        this.memAuthDAO.clear();
        this.memGameDAO.clear();
    }
}
