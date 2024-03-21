package ui;
import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.*;
import com.sun.nio.sctp.NotificationHandler;
import dataAccess.SQLGameDAO;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import model.UserData;
import server.Server;
import ui.ChessBoardUI;
import ui.ServerFacade;
import ui.ClientCommunicator;

import java.io.Console;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    String authToken = null;
    String username = null;
    Scanner scanner = new Scanner(System.in);
    private HashMap<Integer, JsonElement> gamesListHashMap = new HashMap<Integer, JsonElement>();

    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        Client client = new Client();
        try {
            client.preLogin();
        }
        finally {
            server.stop();
        }
    }


    public void preLogin(){
        while (this.authToken == null) {
            System.out.println("1. Help");
            System.out.println("2. Quit");
            System.out.println("3. Login");
            System.out.println("4. Register");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            if (Objects.equals(input, "2")) {
                break;
            }
            evalPreLogin(input);
        }
        postLogin();
    }

    public void postLogin() {
        while (this.authToken != null){
            System.out.println("Gameplay Menu:");
            System.out.println("1. Help");
            System.out.println("2. Logout");
            System.out.println("3. Create Game");
            System.out.println("4. List Games");
            System.out.println("5. Join Game");
            System.out.println("6. Join Observer");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            if (Objects.equals(input, "2")) {
                logout();
                break;
            }
            evalPostLogin(input);
        }
        preLogin();
    }

    public void evalPostLogin(String input) {
        try {
            switch (input) {
                case "1" -> helpPostLogin();
                case "2" -> logout();
                case "3" -> createGame();
                case "4" -> listGames();
                case "5" -> joinGame(true);
                case "6" -> joinGame(false);
                default -> System.out.println("Unknown Command Number");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getPlayerColor(){
        System.out.println("Enter 'White' or 'Black' to specify which player you wish to join");
        return scanner.nextLine().toUpperCase();
    }
    public void evalPreLogin(String input) {
        try {
            switch (input) {
                case "1" -> helpPreLogin();
                case "3" -> login();
                case "4" -> register();
                default -> System.out.println("Unknown Command Number");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void helpPreLogin(){
        System.out.println("Help: If you have a user account, enter 3 to log in. If not, enter 4 to register.");
    }
    public void helpPostLogin(){
        System.out.println("Help: Type a number to execute a command in game mode");
    }

    public void login(){
        try{
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            this.authToken = ServerFacade.login(username, password);
            this.username = username;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void register(){
        try{
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            System.out.println("Enter Email: ");
            String email = scanner.nextLine();
            this.authToken = ServerFacade.register(username, password, email);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logout(){
        try{
            ServerFacade.logout(this.authToken);
            this.authToken = null;
        }
        catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }

    }
    public void createGame(){
        System.out.println("Enter Game Name: ");
        String gameName = scanner.nextLine();
        try{
            String gameID = ServerFacade.createGame(gameName, this.authToken);
            System.out.println("The game was created successfully!\n");
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void listGames(){
        try{
            JsonArray jsonList = ui.ServerFacade.listGames(this.authToken);
            gamesListHashMap.clear();
            System.out.println("List of Games:");
            for (int number = 1; number < jsonList.size() + 1; number++){
                JsonElement jsonElement = jsonList.get(number -1);
                //add into the hashMap
                updateHashMapValue(number, jsonElement);
                Gson gson = new Gson();
                gson.toJson(jsonElement);
                String gameName = jsonElement.getAsJsonObject().get("gameName").toString();
                JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
                JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
                String whiteUser = "none";
                String blackUser = "none";
                if (whiteUserJson != null){
                    whiteUser = whiteUserJson.toString();
                }
                if (blackUserJson != null){
                    blackUser = blackUserJson.toString();
                }
                System.out.println(number + ".  " + "GameID = " + gameName + "  White User: " + whiteUser + "   BlackUser: " + blackUser);
            }
            System.out.println();

        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateHashMapValue(Integer number, JsonElement jsonElement){
        gamesListHashMap.put(number, jsonElement);
    }


    public void joinGame(boolean needsPlayer) {
        try {
            listGames();
            System.out.println("Enter the number of the game you wish to join: ");
            String number = scanner.nextLine();
            boolean validGame = false;
            while (!validGame) {
                JsonElement gameJson = gamesListHashMap.get(Integer.valueOf(number));
                if (gameJson == null){
                    System.out.println("You have chosen an invalid game number. Enter a new valid number: ");
                    number = scanner.nextLine();
                }
                else {
                    validGame = true;
                }
            }
            String playerColor = null;
            //check if we need to get the player
            if (needsPlayer){
                playerColor = getPlayerColor();
                //validate that the player isn't already taken
                if (!playerIsAvailable(number, playerColor)){
                    throw new DataAccessException("The team you've chosen already has a user assigned to it.");
                }
            }
            JsonElement gameJsonElement = gamesListHashMap.get(Integer.valueOf(number));
            JsonObject gameJson= gameJsonElement.getAsJsonObject();
            if (Objects.equals(playerColor, "WHITE")){
                gameJson.add("whiteUsername", new JsonPrimitive(username));
            }
            if (Objects.equals(playerColor, "BLACK")){
                gameJson.add("blackUsername", new JsonPrimitive(username));
            }
            updateHashMapValue(Integer.valueOf(number), gameJson);
            //using the number, get the gameID
            String gameID = gameJson.getAsJsonObject().get("gameID").toString();
            //make the http call
            ServerFacade.joinGame(this.authToken, gameID, playerColor);
            //update the hashMap


            //get the game board
            Gson gson2 = new Gson();
            GameData chessGame = gson2.fromJson(gameJson, GameData.class);
            ChessBoard chessBoard = chessGame.getChessBoard();
            //reset for the sake of phase 5
            chessBoard.resetBoard();
            //display the game board
            ChessBoardUI chessBoardUI = new ChessBoardUI(chessBoard);
            chessBoardUI.drawBoard();



            System.out.println("Type anything to view the Gameplay Menu;");
            scanner.nextLine();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.out.println("Try to execute the command again\n");
        }
    }

    private boolean playerIsAvailable(String number, String desiredColor){
        JsonElement jsonElement = gamesListHashMap.get(Integer.valueOf(number));
        JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
        JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
        String whiteUser = "none";
        String blackUser = "none";
        if (whiteUserJson != null){
            whiteUser = whiteUserJson.toString();
        }
        if (blackUserJson != null){
            blackUser = blackUserJson.toString();
        }
        if (Objects.equals(desiredColor, "WHITE") && Objects.equals(whiteUser, "none")){
            return true;
        }
        if (Objects.equals(desiredColor, "BLACK") && Objects.equals(blackUser, "none")){
            return true;
        }
        return false;
    }

}
