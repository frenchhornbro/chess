package service;

import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;

public class CreateGameService {
    private final SQLAuthDAO memAuthDao;
    private final SQLGameDAO memGameDao;

    public CreateGameService(SQLGameDAO memGameDao, SQLAuthDAO memAuthDao) {
        this.memGameDao = memGameDao;
        this.memAuthDao = memAuthDao;
    }

    public GameData createGame(String gameName, String authToken) throws ServiceException {
        //Check whether the authToken is in the database and return it if it is
        if (gameName == null || authToken == null) throw new ServiceException("Error: bad request", 400);
        AuthData authData = this.memAuthDao.getAuth(authToken);
        if (authData != null) return this.memGameDao.createGame(gameName);
        else throw new ServiceException("Error: unauthorized", 401);
    }
}
