package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataStorage.GamesStorage;

public class ListGamesService {
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLGameDAO sqlGameDAO;

    public ListGamesService() throws Exception {
        this.sqlAuthDAO = new SQLAuthDAO();
        this.sqlGameDAO = new SQLGameDAO();
    }

    public GamesStorage listGames(String authToken) throws ServiceException {
        //Check that user has a valid authToken and if so return a list of all the games
        //Game data to be listed: gameID, whiteUsername, blackUsername, gameName
        try {
            String storedAuthToken = sqlAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);
            return sqlGameDAO.getGames();
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500); //TODO: Make sure 500 is the correct number
        }
    }
}
