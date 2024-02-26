package model;

public class AuthData {
    private int authToken;
    private String username;
    public AuthData(Integer authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }
    public int getAuthToken(){
        return this.authToken;
    }
}
