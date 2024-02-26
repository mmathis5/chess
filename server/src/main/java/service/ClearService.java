package service;

import dataAccess.*;
import model.*;

public class ClearService {
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;
    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }
    public void clear(){
        userDAO.clear();
        gameDAO.clear();
        authDAO.clear();
    }
}

