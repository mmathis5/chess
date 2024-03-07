package dataAccess;
import model.AuthData;

import java.sql.SQLException;

public interface AuthDAO {
    public void clear();
    public String generateAuthToken();
    public void addAuthData(String authToken, AuthData authData);
    public AuthData getAuth(String authToken) throws SQLException;
    public void deleteAuth(String authToken);


}
