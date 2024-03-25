package service;

import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataStorage.GameStorage;

public class JoinGameService {
    private final SQLAuthDAO sqlAuthDAO;
    private final SQLGameDAO sqlGameDAO;

    public JoinGameService() throws Exception {
        this.sqlAuthDAO = new SQLAuthDAO();
        this.sqlGameDAO = new SQLGameDAO();
    }

    public void joinGame(String authToken, String playerColor, int gameID, String username) throws ServiceException {
        //Check if user has a valid authToken, if game exists, and if the color is already allocated
        //If all these conditions pass, add the user as requested color
        try {
            String storedAuthToken = this.sqlAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);

            if (this.sqlGameDAO.gameIsNull(gameID)) throw new ServiceException("Error: bad request", 400);
            this.sqlGameDAO.updateGame(gameID, playerColor, storedAuthToken, username);
        }
        catch(DataAccessException exception) {
            if (exception.getMessage().equals("Error: already taken")) {
                throw new ServiceException(exception.getMessage(), 403);
            }
            else throw new ServiceException(exception.getMessage(), 500);
        }
    }
}
