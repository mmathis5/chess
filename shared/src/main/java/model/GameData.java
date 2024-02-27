package model;

import chess.ChessGame;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameData {
    private Integer gameID;
    private String whiteUsername;

    private String blackUsername;
    private String gameName;
    private final ChessGame chessGame = new ChessGame();
    private ArrayList<String> observers = new ArrayList<String>();

    //init function
    public GameData(String gameName, Integer gameID){
        this.gameName = gameName;
        this.gameID = gameID;

    }

    public void setWhiteUser(String username){
        this.whiteUsername = username;
    }
    public void setBlackUser(String username){
        this.blackUsername = username;
    }

    public String getBlackUser(){
        return this.blackUsername;
    }
    public String getWhiteUser(){
        return this.whiteUsername;
    }
    public void addObserver(String username){
        observers.add(username);
    }


}
