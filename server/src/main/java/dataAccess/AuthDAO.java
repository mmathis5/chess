package dataAccess;
import dataAccess.exceptions.DataAccessException;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    public void clear();
    public String generateAuthToken();
    public void addAuthData(String authToken, AuthData authData) throws SQLException, DataAccessException;
    public AuthData getAuth(String authToken) throws SQLException;
    public void deleteAuth(String authToken) throws SQLException;


}
