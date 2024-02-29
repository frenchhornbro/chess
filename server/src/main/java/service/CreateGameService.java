package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private final MemoryAuthDAO memAuthDao;
    private final MemoryGameDAO memGameDao;

    public CreateGameService(MemoryGameDAO memGameDao, MemoryAuthDAO memAuthDao) {
        this.memGameDao = memGameDao;
        this.memAuthDao = memAuthDao;
    }

    public GameData createGame(String gameName, String authToken) throws ServiceException {
        //Check whether the authToken is in the database
        if (gameName == null || authToken == null) throw new ServiceException("Error: bad request", 400);
        AuthData authData = this.memAuthDao.getAuth(authToken);
        if (authData != null) {
            return this.memGameDao.createGame(gameName);
        }
        else throw new ServiceException("Error: unauthorized", 401);
    }
}
