package dataAccess;

import model.GameData;

import java.sql.*;
import java.util.ArrayList;
import java.sql.SQLException;


public class SQLGameDAO implements GameDAO{
    private Connection connection;
    public SQLGameDAO(){// Initialize the database connection
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/your_database_name", "username", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("TRUNCATE gameTable");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Integer createGame(String authToken, String gameName){

    }
    public ArrayList<GameData> getGamesList(){

    }
    public GameData getGame(Integer gameID){

    }

    public void updateGame(Integer gameID, GameData gameData){

    }

}
