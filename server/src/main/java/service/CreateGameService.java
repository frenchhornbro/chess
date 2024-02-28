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
        AuthData authData = this.memAuthDao.getAuth(authToken);
        if (authData != null) {
            GameData game = this.memGameDao.createGame(gameName);
            return game;
        }
        else throw new ServiceException("Error: unauthorized", 401);
    }
    /*
      TODO: Populate the following errors:
       400 - Error: bad request
       401 - Error: unauthorized
       500 - Error: description
       */
}
