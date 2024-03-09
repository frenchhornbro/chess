package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import model.GameData;

public class JoinGameService {
    private final SQLAuthDAO memAuthDAO;
    private final SQLGameDAO memGameDAO;

    public JoinGameService() throws Exception {
        this.memAuthDAO = new SQLAuthDAO();
        this.memGameDAO = new SQLGameDAO();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws ServiceException {
        //Check if user has a valid authToken, if game exists, and if the color is already allocated
        //If all these conditions pass, add the user as requested color
        try {
            String storedAuthToken = this.memAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);

            if (this.memGameDAO.gameIsNull(gameID)) throw new ServiceException("Error: bad request", 400);
            this.memGameDAO.updateGame(gameID, playerColor, storedAuthToken);
        }
        catch(DataAccessException exception) {
            if (exception.getMessage().equals("Error: already taken")) {
                throw new ServiceException(exception.getMessage(), 403);
            }
            else throw new ServiceException(exception.getMessage(), 500);
        }
    }
}
