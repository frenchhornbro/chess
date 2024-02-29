package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;

public class ListGamesService {
    private final MemoryAuthDAO memAuthDAO;
    private final MemoryGameDAO memGameDAO;

    public ListGamesService(MemoryAuthDAO memAuthDAO, MemoryGameDAO memGameDAO) {
        this.memAuthDAO = memAuthDAO;
        this.memGameDAO = memGameDAO;
    }

    public ArrayList<GameData> listGames(String authToken) throws ServiceException {
        AuthData authData = memAuthDAO.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        return memGameDAO.getGames();
    }
}
