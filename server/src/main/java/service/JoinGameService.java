package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.AuthData;
import model.GameData;

public class JoinGameService {
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public JoinGameService(SQLAuthDAO memAuthDAO, SQLGameDAO memGameDAO) {
        this.memAuthDAO = memAuthDAO;
        this.memGameDAO = memGameDAO;
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ServiceException {
        //Check if user has a valid authToken, if game exists, and if the color is already allocated
        //If all these conditions pass, add the user as requested color
        AuthData authData = this.memAuthDAO.getAuth(authToken);
        if (authData == null) throw new ServiceException("Error: unauthorized", 401);
        GameData game = this.memGameDAO.getGame(gameID);
        if (game == null) throw new ServiceException("Error: bad request", 400);

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
