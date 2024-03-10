package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;

public class LogoutService {
    private final SQLAuthDAO sqlAuthDAO;

    public LogoutService() throws Exception {
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    public void logout(String authToken) throws ServiceException {
        //Check if authToken is correct, and if so log out
        try {
            String storedAuthToken = this.sqlAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);
            this.sqlAuthDAO.deleteAuth(storedAuthToken, true);
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500);
        }
    }
}
