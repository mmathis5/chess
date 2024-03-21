package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.sql.SQLException;


public class SQLGameDAO implements GameDAO{
    private Connection connection;
    private int nextGameID = 1;
    public SQLGameDAO() throws DataAccessException {// Initialize the database connection
        this.connection = DatabaseManager.getConnection();
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("DELETE FROM games");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Integer createGame(String authToken, String gameName){
        Integer gameID = (int) (Math.random() * 10000);
        try {
            GameData gameData = new GameData(gameName, gameID);
            gameData.getChessBoard().resetBoard();
            PreparedStatement statement = connection.prepareStatement("INSERT INTO games (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, gameID);
            statement.setString(2, null);
            statement.setString(3, null);
            statement.setString(4, gameName);
            //turn the gameData into a json before serializing
            Gson gson = new Gson();
            String jsonString = gson.toJson(gameData);
            statement.setString(5, jsonString);
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gameID;
    }
    public ArrayList<GameData> getGamesList(){
        ArrayList<GameData> gamesList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM games");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String gameName = resultSet.getString("gameName");
                int gameID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                String jsonGame = resultSet.getString("game");
                //deserialize the game and put it into a gson
                GameData gsonGameData = new Gson().fromJson(jsonGame, GameData.class);
                gamesList.add(gsonGameData);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return gamesList;
    }
    public GameData getGame(Integer gameID) throws SQLException, IOException, ClassNotFoundException {
        GameData gameData = null;
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM games WHERE gameID=?");
        statement.setInt(1, gameID);
        ResultSet resultSet = statement.executeQuery();
        if (resultSet.next()){
            String gameName = resultSet.getString("gameName");
            String whiteUser = resultSet.getString("whiteUsername");
            String blackUser = resultSet.getString("blackUsername");
            String game = resultSet.getString("game");
            Gson gson = new Gson();
            ChessGame chessGame = gson.fromJson(game, ChessGame.class);
            gameData = new GameData(gameName, gameID);
            gameData.setWhiteUser(whiteUser);
            gameData.setBlackUser(blackUser);
            gameData.setChessGame(chessGame);
        }
        return gameData;
    }

    public void updateGame(Integer gameID, GameData gameData){
        try {
        // Serialize the new GameData object
        Gson gson = new Gson();
        String jsonString = gson.toJson(gameData);

            // Prepare SQL statement
        String query = "UPDATE games SET gameName = ?, whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters for the SQL statement
            statement.setString(1, gameData.getGameName());
            statement.setString(2, gameData.getWhiteUser());
            statement.setString(3, gameData.getBlackUser());
            statement.setString(4, jsonString);
            statement.setInt(5, gameID);

            // Execute the SQL statement
            statement.executeUpdate();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

}
