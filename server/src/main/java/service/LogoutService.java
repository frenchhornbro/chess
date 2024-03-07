package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;

public class LogoutService {
    private final SQLAuthDAO memAuthDao;

    public LogoutService() throws Exception {
        this.memAuthDao = new SQLAuthDAO();
    }

    public void logout(String authToken) throws ServiceException {
        //Check if authToken is correct, and if so log out
        try {
            String storedAuthToken = this.memAuthDao.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);
//            this.memAuthDao.deleteAuth(storedAuthToken);
            //TODO: Uncomment this
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500);
        }
    }
}
