package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;

public class CreateGameService {
    private final SQLAuthDAO memAuthDao;
    private final SQLGameDAO memGameDao;

    public CreateGameService() throws Exception {
        this.memGameDao = new SQLGameDAO();
        this.memAuthDao = new SQLAuthDAO();
    }

    public GameData createGame(String gameName, String authToken) throws ServiceException {
        //Check whether the authToken is in the database and return it if it is
        try {
            if (gameName == null || authToken == null) throw new ServiceException("Error: bad request", 400);
            String storedAuthToken = this.memAuthDao.getAuth(authToken);
            if (storedAuthToken != null) return this.memGameDao.createGame(gameName);
            else throw new ServiceException("Error: unauthorized", 401);
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500); //TODO: Make sure 500 is the correct number
        }
    }
}
