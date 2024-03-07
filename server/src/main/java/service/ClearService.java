package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;

public class ClearService {
    private final SQLUserDAO memUserDAO;
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public ClearService() throws Exception {
        this.memUserDAO = new SQLUserDAO();
        this.memAuthDAO = new SQLAuthDAO();
        this.memGameDAO = new SQLGameDAO();
    }

    public void clear() {
        //Clear all data
        this.memUserDAO.clear();
        this.memAuthDAO.clear();
        this.memGameDAO.clear();
    }
}
