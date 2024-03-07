package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;
import java.util.HashMap;

public class ListGamesService {
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public ListGamesService(SQLAuthDAO memAuthDAO, SQLGameDAO memGameDAO) {
        this.memAuthDAO = memAuthDAO;
        this.memGameDAO = memGameDAO;
    }

    public HashMap<Integer, GameData> listGames(String authToken) throws ServiceException {
        //Check that user has a valid authToken and if so return a list of all the games
        AuthData authData = memAuthDAO.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        return memGameDAO.getGames();
    }
}
