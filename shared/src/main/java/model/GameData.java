package model;

import chess.ChessBoard;
import chess.ChessGame;

import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GameData {
    private Integer gameID;
    private String whiteUsername;

    private String blackUsername;
    private String gameName;

    public ChessGame getChessGame() {
        return chessGame;
    }

    private ChessGame chessGame = new ChessGame();
    private ArrayList<String> observers = new ArrayList<String>();

    //init function
    public GameData(String gameName, Integer gameID) {
        this.gameName = gameName;
        this.gameID = gameID;
        chessGame.getBoard().resetBoard();
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
    public String getGameName(){
        return this.gameName;
    }

    public void setChessGame(ChessGame chessGame){this.chessGame = chessGame;}
    public ChessBoard getChessBoard(){
        return chessGame.getBoard();
    }
}
