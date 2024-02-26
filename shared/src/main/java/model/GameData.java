package model;

import chess.ChessGame;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameData {
    private Integer gameID;
    private String whiteUser = null;

    private String blackUser = null;
    private String gameName;
    private final ChessGame chessGame = new ChessGame();
    private ArrayList<String> observers = new ArrayList<String>();

    //init function
    public GameData(String gameName, Integer gameID){
        this.gameName = gameName;
        this.gameID = gameID;

    }

    public void setWhiteUser(String username){
        this.whiteUser = username;
    }
    public void setBlackUser(String username){
        this.blackUser = username;
    }

    public String getBlackUser(){
        return this.blackUser;
    }
    public String getWhiteUser(){
        return this.whiteUser;
    }
    public void addObserver(String username){
        observers.add(username);
    }


}
