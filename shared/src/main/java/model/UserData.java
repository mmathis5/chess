package model;

public class UserData {
    private String username;
    private String password;
    private String email;
    public UserData(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }

//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
