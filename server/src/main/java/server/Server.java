package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dataAccess.exceptions.*;
import spark.*;
import service.*;
import dataAccess.*;
import model.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Server {
    private static UserService userService;
    private static GameService gameService;
    private static ClearService clearService;

    private final UserDAO userDAO;

//    {
//        try {
//            userDAO = new SQLUserDAO();
//        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private final AuthDAO authDAO;

//    {
//        try {
//            authDAO = new SQLAuthDAO();
//        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    private final GameDAO gameDAO;

//    {
//        try {
//            gameDAO = new SQLGameDAO();
//        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public Server(){
        try {
            DatabaseManager.init();
            gameDAO = new SQLGameDAO();
            userDAO = new SQLUserDAO();
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    public void clearEndpoint(){
        Spark.delete("/db", (request, response) -> {
            try {
                //validate that the AuthToken is legal
                try{
                    String authToken = request.headers("authorization");
                }
                catch(Exception e){
                    throw new DataAccessException("something is wrong with the authorization");
                }
                this.clearService.clear();
                response.status(200);
                return new Gson().toJson(Map.of("message", "deleted"));
            }
            catch (DataAccessException e){
                response.status(401);
                throw e;
            }
            catch (Exception e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });
    }
    private void registerEndpoint(){
        Spark.post("/user", (request, response) -> {
            try {
                //validate that all needed parameters are present
                JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                JsonElement username = jsonObject.get("username");
                JsonElement password = jsonObject.get("password");
                JsonElement email = jsonObject.get("email");
                if (username == null || password == null || email == null){
                    throw new BadRequestException("you are missing one of the required fields");
                }
                UserData user = new Gson().fromJson(request.body(), UserData.class);

                AuthData authData = this.userService.register(user);
                response.status(200);
                return new Gson().toJson(authData);
            }
            catch (DataAccessException e){
                response.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            catch (BadRequestException e){
                response.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error:" + e));
            }
        });
    }

    public void loginEndpoint(){
        Spark.post("/session", (request, response) -> {
            try {
                //get the username and password;
                JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                String username = jsonObject.get("username").getAsString();
                String password = jsonObject.get("password").getAsString();
                AuthData authData = this.userService.login(username, password);
                response.status(200);
                return new Gson().toJson(authData);
            }
            catch (DataAccessException e){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });
    }

    public void logoutEndpoint(){
        Spark.delete("/session", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                this.userService.logout(authToken);
                response.status(200);
                return new Gson().toJson(Map.of("message", "logout successful"));
            }
            catch (DataAccessException e){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });
    }

    private void newGameEndpoint(){
        Spark.post("/game", (request, response) -> {
            try {
                try {
                    JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                }
                catch(Exception e){
                    throw new BadRequestException("Something is wrong with the request");
                }
                //get the authToken
                try {
                    String authToken = request.headers("authorization");
                }
                catch(Exception e){
                    throw new BadRequestException("something is wrong with the request");
                }
                JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                String authToken = request.headers("authorization");
                JsonElement gameNameJsonElement = jsonObject.get("gameName");
                if (gameNameJsonElement == null){
                    throw new BadRequestException("Game name not provided");
                }
                String gameName =  jsonObject.get("gameName").getAsString();
                if (!gameNameAvailable(gameName, authToken)){
                    throw new GameNameTakenException("Game name is already taken");
                }
                Integer gameID = this.gameService.createGame(authToken, gameName);
                response.status(200);
                return new Gson().toJson(Map.of("gameID", gameID));
            }
            catch (DataAccessException e){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            catch (BadRequestException e){
                response.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });
    }

    private Boolean gameNameAvailable(String gameName, String authToken) throws InternalFailureException, DataAccessException {
        ArrayList<GameData> allGames = this.gameService.listGames(authToken);
        for (int i = 0; i < allGames.size(); i++) {
            String gameNameFromList = allGames.get(i).getGameName();
            if (Objects.equals(gameNameFromList, gameName)) {
                throw new DataAccessException("There is already a game in play with the same name");
            }
        }
        return true;
    }
    private void listGameEndpoint(){
        Spark.get("/game", (request, response) -> {
            try {
                String authToken = request.headers("authorization");
                ArrayList<GameData> gameList = this.gameService.listGames(authToken);
                return new Gson().toJson(Map.of("games", gameList));
            }
            catch (DataAccessException e){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: unauthorized"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });
    }

    private void joinGameEndpoint(){
        Spark.put("/game", (request, response) -> {
            try {
                JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                //get the authtoken
                try {
                    String authToken = request.headers("authorization");
                }
                catch(Exception e){
                    throw new BadRequestException("something is wrong with the request");
                }
                String authToken = request.headers("authorization");
                try {
                    //case where playerColor is provided
                    String playerColor = jsonObject.get("playerColor").getAsString();
                    Integer gameID = jsonObject.get("gameID").getAsInt();
                    this.gameService.joinGame(gameID, playerColor, authToken);
                }
                catch (NullPointerException e){
                    //case where playerColor is not provided
                    Integer gameID = jsonObject.get("gameID").getAsInt();
                    this.gameService.joinGame(gameID, null, authToken);
                }
                Integer gameID = jsonObject.get("gameID").getAsInt();
                return new Gson().toJson(Map.of("gameID", gameID));
            }
            catch (BadRequestException e){
                response.status(400);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            catch (DataAccessException e){
                response.status(401);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
            catch (JoinGameColorException e){
                response.status(403);
                return new Gson().toJson(Map.of("message", "Error: bad request"));
            }
            catch (InternalFailureException e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });

    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");

        clearEndpoint();
        registerEndpoint();
        loginEndpoint();
        logoutEndpoint();
        newGameEndpoint();
        listGameEndpoint();
        joinGameEndpoint();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) throws SQLException, DataAccessException {
        Server server = new Server();
        server.run(8080);
        WSServer wSServer = new WSServer();
        wSServer.run(8081);
    }
}
