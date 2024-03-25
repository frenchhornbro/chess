package service;

import chess.ChessBoard;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;

public class GetBoardService {
    public ChessBoard getBoard(String authToken, int gameID) throws ServiceException {
        try {
            //Make sure we have correct authorization
            SQLAuthDAO sqlAuthDAO = new SQLAuthDAO();
            String storedAuthToken = sqlAuthDAO.getAuth(authToken);
            if (storedAuthToken == null) throw new ServiceException("Error: unauthorized", 401);

            SQLGameDAO sqlGameDAO = new SQLGameDAO();
            return sqlGameDAO.getBoard(gameID);
        }
        catch (Exception exception) {
            throw new ServiceException(exception.getMessage(), 500);
        }
    }
}
