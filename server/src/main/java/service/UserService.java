package service;
import dataAccess.*;
import model.*;

public class UserService {
    private final DataAccess dataAccess;

    public UserService() {
        this.dataAccess = new MemoryDataAccess();
    }

        public AuthData login(UserData.username username, UserData.password password) throws DataAccessException{
            return dataAccess.login(username, password);
        }

    }

}
