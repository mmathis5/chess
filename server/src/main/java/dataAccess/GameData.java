package dataAccess;

public class GameData {
    private String gameID = null;
    private String whiteUsername = null;

    private String blackUsername = null;
    private String gameName;
    // idk how to do this one
    //    private ChessGame new(ChessGame);

    //init function
    public GameData(){

    }

    void setGameID(String id){
        this.gameID = id;
    }
    void setWhiteUsername(String username){
        this.whiteUsername = username;
    }
    void setBlackUsername(String username){
        this.blackUsername = username;
    }
    void setGameName(String gameName){
        this.gameName = gameName;
    }


}