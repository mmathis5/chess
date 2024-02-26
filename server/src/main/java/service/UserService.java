package service;
import dataAccess.*;
import model.*;

public class UserService {
    private UserDAO userDAO;

    public UserService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

//        public AuthData login(UserData.username username, UserData.password password) throws DataAccessException{
//            return dataAccess.login(username, password);
//        }

    }

}
