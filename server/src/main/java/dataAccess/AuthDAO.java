package dataAccess;
import model.AuthData;

public interface AuthDAO {
    public void clear();
    public String generateAuthToken();
    public void addAuthData(String authToken, AuthData authData);
    public AuthData getAuth(String authToken);
    public void deleteAuth(String authToken);


}
