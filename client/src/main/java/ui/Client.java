package ui;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;
import com.google.gson.*;
import dataAccess.exceptions.DataAccessException;
import model.GameData;
import server.Server;

import java.util.*;

public class Client implements ServerMessageObserver {
    ChessBoardUI chessBoardUI = new ChessBoardUI();
    Boolean inGameplayMode = false;
    String localPlayerColor = null;
    ChessBoard localGameBoard = null;
    String authToken = null;
    String username = null;
    JsonArray jsonOfGames;
    Scanner scanner = new Scanner(System.in);
    private ServerFacade serverFacade;
    private HashMap<Integer, JsonElement> gamesListHashMap = new HashMap<Integer, JsonElement>();

    public Client(){
        this.serverFacade = new ServerFacade(8080, this);
    }

    public void onMessage(String msg){

    }
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
                return;
            }
            evalPreLogin(input);
        }
        postLogin();
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



    public void postLogin() {
        while (this.authToken != null){
            System.out.println("Post Login Menu:");
            System.out.println("1. Help");
            System.out.println("2. Logout");
            System.out.println("3. Create Game");
            System.out.println("4. List Games");
            System.out.println("5. Join a Game");
            System.out.println("6. Join a Game As An Observer");
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

    public void gameplayMode(){
        while (inGameplayMode){
            System.out.println("Gameplay Menu:");
            System.out.println("1. Help");
            System.out.println("2. Redraw Chess Board");
            System.out.println("3. Leave");
            System.out.println("4. Make Move");
            System.out.println("5. Resign");
            System.out.println("6. Highlight Legal Moves");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            evalGameplayMode(input);
        }
    }

    public void evalGameplayMode(String input) {
        try {
            switch (input) {
                case "1" -> helpGameplayMode();
                //case "2" -> redrawChessBoard();
                case "3" -> leaveGame();
                case "6" -> highlightLegalMoves();
                default -> System.out.println("Command not yet implemented");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getPlayerColor(){
        System.out.println("Enter 'White' or 'Black' to specify which player you wish to join");
        return scanner.nextLine().toUpperCase();
    }

    public void helpPreLogin(){
        System.out.println("Help: If you have a user account, enter 3 to log in. If not, enter 4 to register.");
    }
    public void helpPostLogin(){
        System.out.println("Help: Type a number to create, view, or join a game");
    }

    public void helpGameplayMode(){
        System.out.println("Help: Type a number to execute a command in game mode");
    }
    public void login(){
        try{
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            this.authToken = serverFacade.login(username, password);
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
            this.username = username;
            this.authToken = serverFacade.register(username, password, email);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logout(){
        try{
            serverFacade.logout(this.authToken);
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
            String gameID = serverFacade.createGame(gameName, this.authToken);
            System.out.println("The game was created successfully!\n");
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void getGamesJson(){
        try{
            this.jsonOfGames = serverFacade.listGames(this.authToken);
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void listGames(){
        try{
            getGamesJson();
            gamesListHashMap.clear();
            System.out.println("List of Games:");
            for (int number = 1; number < this.jsonOfGames.size() + 1; number++){
                JsonElement jsonElement = this.jsonOfGames.get(number -1);
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


    public void isValidGame(String number){
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
    }
    public void joinGame(boolean needsPlayer) {
        try {
            getGamesJson();
            if (this.jsonOfGames.isEmpty()){
                throw new DataAccessException("There are no created games for you to choose from. Create a game, then try again.");
            }
            listGames();
            System.out.println("Enter the number of the game you wish to join: ");
            String number = scanner.nextLine();
            isValidGame(number);
            String playerColor = null;
            //check if we need to get the player
            if (needsPlayer){
                playerColor = getPlayerColor();
                //validate that the player isn't already taken
                if (!playerIsAvailable(number, playerColor)){
                    throw new DataAccessException("The team you've chosen already has a user assigned to it.\nTry to execute the command again");
                }
            }
            JsonObject gameJson = gamesListHashMap.get(Integer.valueOf(number)).getAsJsonObject();
            if (Objects.equals(playerColor, "WHITE")){
                gameJson.add("whiteUsername", new JsonPrimitive(username));
                this.localPlayerColor = "WHITE";
            }
            if (Objects.equals(playerColor, "BLACK")){
                gameJson.add("blackUsername", new JsonPrimitive(username));
                this.localPlayerColor = "BLACK";
            }
            updateHashMapValue(Integer.valueOf(number), gameJson);
            //using the number, get the gameID
            String gameID = gameJson.getAsJsonObject().get("gameID").toString();
            //make the http call
            serverFacade.joinGame(this.authToken, gameID, playerColor);
            getGamesJson();
            //update the hashMap


            //get the game board
            Gson gson2 = new Gson();
            GameData chessGame = gson2.fromJson(gameJson, GameData.class);
            this.inGameplayMode = true;
            this.chessBoardUI.setChessBoard(chessGame.getChessBoard());
            drawBoardProperOrientation(chessGame.getChessBoard());


            gameplayMode();
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    private boolean playerIsAvailable(String number, String desiredColor){
        JsonElement jsonElement = gamesListHashMap.get(Integer.valueOf(number));
        if (Objects.equals(desiredColor, "WHITE")){
            JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
            return whiteUserJson == null;
        }
        else if (Objects.equals(desiredColor, "BLACK")){
            JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
            return blackUserJson == null;
        }
        return false;
    }

    private void drawBoardProperOrientation(ChessBoard chessBoard){
        this.chessBoardUI.setChessBoard(chessBoard);
        if (Objects.equals(this.localPlayerColor, "WHITE") || this.localPlayerColor == null){
            //print with white at the bottom
            this.chessBoardUI.drawBoardWhite();
        }
        else{
            //print it with black at the bottom
            this.chessBoardUI.drawBoardBlack();
        }
    }

//    private void redrawChessBoard(){
//        drawBoardProperOrientation();
//    }

    private void leaveGame(){
        if (localPlayerColor == null) {
            System.out.println("\nYou have successfully exited the game you were observing. \nWelcome back to the post login menu.\n");

            this.inGameplayMode = false;
            postLogin();
        }
        else{
            System.out.println("You are an active player and as such, cannot leave the game. Choose a valid command.\n");
        }
    }

    private void highlightLegalMoves(){
        //initialize a hash map to get the coordinates in numeric form
        HashMap<String, Integer> letterToNumber = new HashMap<String, Integer>();
        letterToNumber.put("a", 1);
        letterToNumber.put("b", 2);
        letterToNumber.put("c", 3);
        letterToNumber.put("d", 4);
        letterToNumber.put("e", 5);
        letterToNumber.put("f", 6);
        letterToNumber.put("g", 7);
        letterToNumber.put("h", 8);
        //query the client for the piece it wishes to see the moves for
        System.out.println("What is the letter coordinate of the piece you wish to see legal moves for?");
        Integer letterCor = letterToNumber.get(scanner.nextLine().toLowerCase());
        System.out.println("What is the number coordinate of the piece you wish to see legal moves for?");
        Integer numberCor = Integer.parseInt(scanner.nextLine());
        ChessPiece currPiece = localGameBoard.getPiece(new ChessPosition(letterCor, numberCor));
        Collection<ChessMove> possibleMoves =currPiece.pieceMoves(localGameBoard, new ChessPosition(letterCor, numberCor));
        System.out.println(currPiece.getPieceType());
    }
}
