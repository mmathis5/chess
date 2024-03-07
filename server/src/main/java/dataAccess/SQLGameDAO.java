package dataAccess;

import chess.ChessGame;
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
        configureDatabase();
    }
    public void clear(){
        try{
            PreparedStatement statement = connection.prepareStatement("DROP table gameTable");
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e){
            e.printStackTrace();
        }
    }
    public Integer createGame(String authToken, String gameName){
        Integer gameID = nextGameID;
        nextGameID = nextGameID++;
        try {
            GameData gameData = new GameData(gameName, gameID);
            PreparedStatement statement = connection.prepareStatement("INSERT INTO gameTable (gameID, whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?, ?)");
            statement.setInt(1, gameID);
            statement.setString(2, null);
            statement.setString(3, null);
            statement.setString(4, gameName);
            statement.setBytes(5, gameData.serialize());
            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return gameID;
    }
    public ArrayList<GameData> getGamesList(){
        ArrayList<GameData> gamesList = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM gameTable");
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String gameName = resultSet.getString("gameName");
                int gameID = resultSet.getInt("gameID");
                String whiteUsername = resultSet.getString("whiteUsername");
                String blackUsername = resultSet.getString("blackUsername");
                byte[] serializedGameData = resultSet.getBytes("game");

                GameData gameData = GameData.deserialize(serializedGameData);

                gamesList.add(gameData);
            }

            // Close the result set and statement
            resultSet.close();
            statement.close();
        } catch (SQLException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return gamesList;
    }
    public GameData getGame(Integer gameID) throws SQLException, IOException, ClassNotFoundException {
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM gameTable WHERE gameID=?");
        statement.setInt(1, gameID);
        ResultSet resultSet = statement.executeQuery();
        byte[] serializedGameData = resultSet.getBytes("gameData");
        GameData gameData = GameData.deserialize(serializedGameData);
        return gameData;
    }

    public void updateGame(Integer gameID, GameData gameData){
        try {
        // Serialize the GameData object
        byte[] serializedGameData = gameData.serialize();

        // Prepare SQL statement
        String query = "UPDATE gameTable SET gameName = ?, whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            // Set parameters for the SQL statement
            statement.setString(1, gameData.getGameName());
            statement.setString(2, gameData.getWhiteUser());
            statement.setString(3, gameData.getBlackUser());
            statement.setBytes(4, serializedGameData);
            statement.setInt(5, gameID);

            // Execute the SQL statement
            statement.executeUpdate();
        }
    } catch (SQLException | IOException e) {
        e.printStackTrace();
    }
    }

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            DatabaseManager.createGameTable(conn);
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }

}
