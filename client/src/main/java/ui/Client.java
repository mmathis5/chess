package ui;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;
import com.google.gson.*;
//import dataAccess.exceptions.DataAccessException;
import model.GameData;
//import server.Server;
import ui.DrawingChessBoard.ChessBoardUI;
import webSocketMessages.Error;
import webSocketMessages.LoadGame;
import webSocketMessages.Notification;
import webSocketMessages.ServerMessage;
import websocket.NotificationHandler;
import websocket.WebSocketFacade;

import java.io.IOException;
import java.util.*;

public class Client implements NotificationHandler {
    ChessBoardUI chessBoardUI = new ChessBoardUI();
    Boolean inGameplayMode = false;
    String localPlayerColor = null;
    String authToken = null;
    String username = null;
    Integer gameNumber;
    Integer gameID;
    JsonArray jsonOfGames;
    Scanner scanner = new Scanner(System.in);
    private final String serverURL = "http:localhost:8080";
    private ServerFacade serverFacade;
    private WebSocketFacade ws;
    private JsonObject jsonMapper = new JsonObject();

    private HashMap<Integer, JsonElement> gamesListHashMap = new HashMap<Integer, JsonElement>();

    public Client(String serverURL) {
        this.serverFacade = new ServerFacade(serverURL);
    }

    public void notify(String message, ServerMessage.ServerMessageType type){
        System.out.println("message recieved: " + type);
        System.out.println(message);
        System.out.println(type+ ": " + message);

        if (type == ServerMessage.ServerMessageType.LOAD_GAME){
            redrawChessBoard();
        }
        else if (type == ServerMessage.ServerMessageType.NOTIFICATION){
            Notification notification = new Gson().fromJson(message, Notification.class);
            System.out.println("Notification: " + notification.getMessage());
        }
        else if(type ==  ServerMessage.ServerMessageType.ERROR){
            Error notification = new Gson().fromJson(message, Error.class);
            System.out.println("Error: " + notification.getErrorMessage());
        }

    }

    public void preLogin() {
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
            }
            ;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public void postLogin() {
        while (this.authToken != null) {
            if (this.inGameplayMode){
                gameplayMode();
            }
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
                case "5" -> joinGame(true, true);
                case "6" -> joinGame(false, true);
                default -> System.out.println("Unknown Command Number");
            }
            ;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void gameplayMode() {
        while (inGameplayMode) {
            System.out.println("Gameplay Menu:");
            System.out.println("1. Help");
            System.out.println("2. Redraw Chess Board");
            System.out.println("3. Leave");
            System.out.println("4. Make Move");
            System.out.println("5. Resign");
            System.out.println("6. Highlight Legal Moves");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            if (Objects.equals(input, "5")) {
                resign();
                inGameplayMode = false;
                break;
            }
            if (Objects.equals(input, "3")) {
                leaveGame();
                inGameplayMode = false;
                break;
            }
            evalGameplayMode(input);
        }
        postLogin();
    }

    public void evalGameplayMode(String input) {
        try {
            switch (input) {
                case "1" -> helpGameplayMode();
                case "2" -> redrawChessBoard();
                case "3" -> leaveGame();
                case "4" -> makeMove();
                case "5" -> resign();
                case "6" -> highlightLegalMoves();
                default -> System.out.println("Command not yet implemented");
            }
            ;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private String getPlayerColor() {
        System.out.println("Enter 'White' or 'Black' to specify which player you wish to join");
        return scanner.nextLine().toUpperCase();
    }

    public void helpPreLogin() {
        System.out.println("Help: If you have a user account, enter 3 to log in. If not, enter 4 to register.");
    }

    public void helpPostLogin() {
        System.out.println("Help: Type a number to create, view, or join a game");
    }

    public void helpGameplayMode() {
        System.out.println("Help: Type a number to execute a command in game mode");
    }

    public void login() {
        try {
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            this.authToken = serverFacade.login(username, password);
            this.username = username;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void register() {
        try {
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            System.out.println("Enter Email: ");
            String email = scanner.nextLine();
            this.username = username;
            this.authToken = serverFacade.register(username, password, email);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logout() {
        try {
            serverFacade.logout(this.authToken);
            this.authToken = null;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void createGame() {
        System.out.println("Enter Game Name: ");
        String gameName = scanner.nextLine();
        try {
            String gameID = serverFacade.createGame(gameName, this.authToken);
            System.out.println("The game was created successfully!\n");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    public void getGamesJson() {
        try {
            this.jsonOfGames = serverFacade.listGames(this.authToken);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listGames() {
        try {
            getGamesJson();
            gamesListHashMap.clear();
            System.out.println("List of Games:");
            for (int number = 1; number < this.jsonOfGames.size() + 1; number++) {
                JsonElement jsonElement = this.jsonOfGames.get(number - 1);
                //add into the hashMap
                updateHashMapValue(number, jsonElement);
                Gson gson = new Gson();
                gson.toJson(jsonElement);
                String gameName = jsonElement.getAsJsonObject().get("gameName").toString();
                JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
                JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
                String whiteUser = "none";
                String blackUser = "none";
                if (whiteUserJson != null) {
                    whiteUser = whiteUserJson.toString();
                }
                if (blackUserJson != null) {
                    blackUser = blackUserJson.toString();
                }
                System.out.println(number + ".  " + "GameID = " + gameName + "  White User: " + whiteUser + "   BlackUser: " + blackUser);
            }
            System.out.println();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void updateHashMapValue(Integer number, JsonElement jsonElement) {
        gamesListHashMap.put(number, jsonElement);
    }


    public void isValidGame(String number) {
        boolean validGame = false;
        while (!validGame) {
            JsonElement gameJson = gamesListHashMap.get(Integer.valueOf(number));
            if (gameJson == null) {
                System.out.println("You have chosen an invalid game number. Enter a new valid number: ");
                number = scanner.nextLine();
            } else {
                validGame = true;
            }
        }
    }


    public void joinGame(boolean needsPlayer, Boolean trueJoin) {
        try {
            if (trueJoin){
                getGamesJson();
                if (this.jsonOfGames.isEmpty()) {
                    throw new Exception("There are no created games for you to choose from. Create a game, then try again.");
                }
                listGames();
                System.out.println("Enter the number of the game you wish to join: ");
                String number = scanner.nextLine();
                this.gameNumber = Integer.parseInt(number);
                isValidGame(number);
                this.gameNumber = Integer.parseInt(number);
                String playerColor = null;
                //check if we need to get the player
                if (needsPlayer) {
                    playerColor = getPlayerColor();
                    //validate that the player isn't already taken
                    if (!playerIsAvailable(number, playerColor)) {
                        throw new Exception("The team you've chosen already has a user assigned to it.\nTry to execute the command again");
                    }
                    this.localPlayerColor = playerColor;
                }
            }
            JsonObject gameJson = gamesListHashMap.get(this.gameNumber).getAsJsonObject();
            if (Objects.equals(localPlayerColor, "WHITE")) {
                gameJson.add("whiteUsername", new JsonPrimitive(username));
                this.localPlayerColor = "WHITE";
            }
            if (Objects.equals(localPlayerColor, "BLACK")) {
                gameJson.add("blackUsername", new JsonPrimitive(username));
                this.localPlayerColor = "BLACK";
            }
            updateHashMapValue(this.gameNumber, gameJson);
            //using the number, get the gameID
            String gameID = gameJson.getAsJsonObject().get("gameID").toString();
            this.gameID = Integer.parseInt(gameID);
            //call the http thing
            this.serverFacade.joinGame(authToken, gameID, localPlayerColor);

            //try to make the dumb web socket connection
            ws = new WebSocketFacade(serverURL, this);
            if (!trueJoin){
                return;
            }

            //fix distinct observer vs player
            if (needsPlayer) {
                ws.joinPlayer(this.authToken, gameID, localPlayerColor);
            }
            else{
                ws.joinObserver(this.authToken, gameID);
            }
            this.inGameplayMode = true;

            gameplayMode();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private boolean playerIsAvailable(String number, String desiredColor) {
        JsonElement jsonElement = gamesListHashMap.get(Integer.valueOf(number));
        if (Objects.equals(desiredColor, "WHITE")) {
            JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
            try{
                if (Objects.equals(whiteUserJson.toString().substring(1, whiteUserJson.toString().length() - 1), username) || whiteUserJson.isJsonNull()){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                return false;
            }
        }
        else if (Objects.equals(desiredColor, "BLACK")) {
            JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
            try{
                if (Objects.equals(blackUserJson.toString().substring(1, blackUserJson.toString().length() - 1), username) || blackUserJson.isJsonNull()){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                return false;
            }
        }
        return false;
    }

    private void drawBoardProperOrientation(ChessBoard chessBoard, Boolean highlightMoves) {
        this.chessBoardUI.setChessBoard(chessBoard);
        if (Objects.equals(this.localPlayerColor, "WHITE") || this.localPlayerColor == null) {
            //print with white at the bottom
            this.chessBoardUI.drawBoardWhite(highlightMoves);
        } else {
            //print it with black at the bottom
            this.chessBoardUI.drawBoardBlack(highlightMoves);
        }
        System.out.println("\n");
    }

    private void redrawChessBoard() {
        try{
            joinGame(false, false);
            ChessBoard currBoard = getCurrBoard();
            drawBoardProperOrientation(currBoard, false);
        }
        catch (Exception e){
            System.out.println("Error drawBoard: " + e.getMessage());
        }


    }

    private ChessBoard getCurrBoard() {
        //get the most recent board and update the local variable
        getGamesJson();
        JsonObject gameJson = gamesListHashMap.get(this.gameNumber).getAsJsonObject();
        updateHashMapValue(this.gameNumber, gameJson);
        Gson gson = new Gson();
        GameData gameData = gson.fromJson(gameJson, GameData.class);
        return gameData.getChessBoard();
    }


    public ChessPosition getValidCoordinates(String command) {
        ChessBoard currentBoard = getCurrBoard();
        HashMap<String, Integer> correctColorHashMap;
        //initialize a hash map to get the coordinates in numeric form
        HashMap<String, Integer> letterToNumberWhite = new HashMap<String, Integer>();
        letterToNumberWhite.put("a", 1);
        letterToNumberWhite.put("b", 2);
        letterToNumberWhite.put("c", 3);
        letterToNumberWhite.put("d", 4);
        letterToNumberWhite.put("e", 5);
        letterToNumberWhite.put("f", 6);
        letterToNumberWhite.put("g", 7);
        letterToNumberWhite.put("h", 8);


        ArrayList<Integer> possibleRows = new ArrayList<>();
        possibleRows.add(1);
        possibleRows.add(2);
        possibleRows.add(3);
        possibleRows.add(4);
        possibleRows.add(5);
        possibleRows.add(6);
        possibleRows.add(7);
        possibleRows.add(8);

        //query the client for the piece it wishes to see the moves for
        correctColorHashMap = letterToNumberWhite;


        System.out.println("What is the letter coordinate of the " + command);
        Boolean validLetterCoordinate = false;
        Integer letterCor = 0;
        while (!validLetterCoordinate) {
            String letterCorString = scanner.nextLine().toLowerCase();
            if (correctColorHashMap.containsKey(letterCorString)) {
                validLetterCoordinate = true;
                letterCor = correctColorHashMap.get(letterCorString);
            } else {
                System.out.println("this is an invalid Coordinate. Try Again.");
            }
        }
        System.out.println("What is the number coordinate of " + command);
        Integer numberCor = Integer.parseInt(scanner.nextLine());
        Boolean validNumberCoordinate = false;
        while (!validNumberCoordinate) {
            if (possibleRows.contains(numberCor)) {
                validNumberCoordinate = true;
            } else {
                System.out.println("This is an invalid number coordinate. Enter a valid number now:");
                numberCor = Integer.parseInt(scanner.nextLine());
            }
        }
        return new ChessPosition(numberCor, letterCor);
    }

    private void highlightLegalMoves() {
        ChessBoard currentBoard = getCurrBoard();
        ChessPosition selectedPosition = getValidCoordinates("piece you wish to see legal moves for?");
        ChessPiece currPiece = currentBoard.getPiece(selectedPosition);
        Collection<ChessMove> possibleMoves = currPiece.pieceMoves(currentBoard, selectedPosition);
        this.chessBoardUI.setPossibleMoves(possibleMoves);
        this.chessBoardUI.setStartingPosition(selectedPosition);

        System.out.print("The legal moves for your selected piece are highlighted below:");
        drawBoardProperOrientation(currentBoard, true);

    }

    private void makeMove() throws Exception {
        //make sure the game is in progress
        try {
            getGamesJson();
            JsonObject gameJson = gamesListHashMap.get(this.gameNumber).getAsJsonObject();
            updateHashMapValue(this.gameNumber, gameJson);
            Gson gson = new Gson();
            GameData gameData = gson.fromJson(gameJson, GameData.class);
            if (gameData.getGameIsComplete()) {
                new Error("This game is complete. Please leave the game.");
            }

            ChessPosition selectedPosition = getValidCoordinates("piece you wish to move?");
            ChessPosition newLocation = getValidCoordinates("location you wish to move the piece to?");
            System.out.println("Do you wish to add a promotional piece to this move? (y/n)");
            String promoPieceBool = scanner.nextLine().toLowerCase();
            ChessPiece.PieceType promoPiece = null;
            if (promoPieceBool == "y") {
                System.out.println("What piece do you wish to promote?");
                String promoPieceTypeString = scanner.nextLine().toLowerCase();
                if (promoPieceTypeString.equals("queen")) {
                    promoPiece = ChessPiece.PieceType.QUEEN;
                }
                if (promoPieceTypeString.equals("king")) {
                    promoPiece = ChessPiece.PieceType.KING;
                }
                if (promoPieceTypeString.equals("knight")) {
                    promoPiece = ChessPiece.PieceType.KNIGHT;
                }
                if (promoPieceTypeString.equals("rook")) {
                    promoPiece = ChessPiece.PieceType.ROOK;
                }
                if (promoPieceTypeString.equals("bishop")) {
                    promoPiece = ChessPiece.PieceType.BISHOP;
                }
            }
            ChessMove desiredMove = new ChessMove(selectedPosition, newLocation, promoPiece);
            ws.makeMove(authToken, gameID, desiredMove);
        }
        catch (Exception e){
            System.out.println("Error Make Move: " + e.getMessage());
        }
    }
    private void resign(){
        System.out.println("Are you sure you wish to resign? Type 'yes' to continue:");
        String response = scanner.nextLine().toLowerCase();
        if (!Objects.equals(response, "yes")){
            System.out.println("Returning to the gameplay menu:");
            return;
        }
        try{
            ws.resign(authToken, gameID);
        }
        catch (Exception e){
            System.out.println("Error resign: " + e.getMessage());
        }
    }


    private void leaveGame() {
        try {
            ws.leaveGame(authToken, gameID);
            System.out.println("\nYou have successfully exited the game you were observing. \nWelcome back to the post login menu.\n");
            this.inGameplayMode = false;
        }
        catch (Exception e){
            System.out.println("Error leaveGame:" + e.getMessage());
        }
    }
}

