package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;

public class CreateGameService {
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLGameDAO sqlGameDao;

    public CreateGameService() throws Exception {
        this.sqlGameDao = new SQLGameDAO();
        this.sqlAuthDAO = new SQLAuthDAO();
    }

    public int createGame(String gameName, String authToken) throws ServiceException {
        //Check whether the authToken is in the database and return gameID if it is
        try {
            if (gameName == null || authToken == null) throw new ServiceException("Error: bad request", 400);
            String storedAuthToken = this.sqlAuthDAO.getAuth(authToken);
            if (storedAuthToken != null) return this.sqlGameDao.createGame(gameName);
            else throw new ServiceException("Error: unauthorized", 401);
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500); //TODO: Make sure 500 is the correct number
        }
    }
}
