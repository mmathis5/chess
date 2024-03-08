package model;

public class AuthData {
    private String authToken;
    private String username;
    public AuthData(String authToken, String username){
        this.authToken = authToken;
        this.username = username;
    }
    public String getUsername(){return this.username;}
    public String getAuthToken(){return this.authToken;}

}
