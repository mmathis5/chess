package server;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.protobuf.Internal;
import dataAccess.Exceptions.*;
import spark.*;
import service.*;
import dataAccess.*;
import model.*;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    public Server(){
        this.userService = new UserService(userDAO, authDAO);
        this.gameService = new GameService(gameDAO, authDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //clear
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

        //register
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
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
        });

        //login
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

        //logout
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

        //New Game
        Spark.post("/game", (request, response) -> {
            try {
                try {
                    JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                }
                catch(Exception e){
                    throw new BadRequestException("Something is wrong with the request");
                }
                //get the authtoken
                try {
                    String authToken = request.headers("authorization");
                }
                catch(Exception e){
                    throw new BadRequestException("something is wrong with the request");
                }
                JsonObject jsonObject = new Gson().fromJson(request.body(), JsonObject.class);
                String authToken = request.headers("authorization");
                String gameName =  jsonObject.get("gameName").getAsString();
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

        //listGames endpoint
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

        //JoinGame
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
                String playerColor = jsonObject.get("playerColor").getAsString();
                Integer gameID = jsonObject.get("gameID").getAsInt();
                this.gameService.joinGame(gameID, playerColor, authToken);

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



        // Register your endpoints and handle exceptions here.


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.run(8080);
    }
}
