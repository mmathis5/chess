package dataAccess;
import model.AuthData;

public interface AuthDAO {
    public void clear();
    public String generateAuthToken();
    public AuthData getAuth(AuthData authData);
    public void deleteAuth(AuthData authData);


}
