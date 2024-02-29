package service;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private final MemoryAuthDAO memAuthDAO;
    private final MemoryGameDAO memGameDAO;

    public JoinGameService(MemoryAuthDAO memAuthDAO, MemoryGameDAO memGameDAO) {
        this.memAuthDAO = memAuthDAO;
        this.memGameDAO = memGameDAO;
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ServiceException {
        AuthData authData = this.memAuthDAO.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        GameData game = this.memGameDAO.getGame(gameID);
        if (game == null/* || (game.getWhiteUsername() == null && game.getBlackUsername() == null && game.getGame() == null)*/)
            throw new ServiceException("Error: bad request", 400);
        try {
            this.memGameDAO.updateGame(game, playerColor, authData.getUsername());
        }
        catch(DataAccessException exception) {
            if (exception.getMessage().equals("Error: already taken")) {
                throw new ServiceException(exception.getMessage(), 403);
            }
        }
    }
}
