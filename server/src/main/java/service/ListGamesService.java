package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;
import java.util.HashMap;

public class ListGamesService {
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public ListGamesService() throws Exception {
        this.memAuthDAO = new SQLAuthDAO();
        this.memGameDAO = new SQLGameDAO();
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws ServiceException {
        //Check that user has a valid authToken and if so return a list of all the games
        try {
            String storedAuthToken = memAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);
            return memGameDAO.getGames();
        }
        catch (DataAccessException ex) {
            throw new ServiceException(ex.getMessage(), 500); //TODO: Make sure 500 is the correct number
        }
    }
}
