package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataAccess.Exceptions.*;
import spark.*;
import service.*;
import dataAccess.*;
import model.*;

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
        this.gameService = new GameService(gameDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        //clear
        Spark.delete("/db", (request, response) -> {
            try {
                this.clearService.clear();
                response.status(200);
                return new Gson().toJson(Map.of("message", "deleted"));
            }
            catch (Exception e){
                response.status(500);
                return new Gson().toJson(Map.of("message", "Error: description"));
            }
            });

        //register
        Spark.post("/user", (request, response) -> {
            try {
                UserData user = new Gson().fromJson(request.body(), UserData.class);

                AuthData authData = this.userService.register(user);
                response.status(200);
                return new Gson().toJson(authData);
            }
            catch (UsernameExistsException e){
                response.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
//            catch (BadRequestException e){
//                response.status(400);
//                return new Gson().toJson(Map.of("message", "Error: bad request"));
//            }
//            catch (FailureException e){
//                response.status(500);
//                return new Gson().toJson(Map.of("message", "Error: description"));
//            }
        //still need 400 and 500 requests. Idk where they come from
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

        //register
        Spark.post("/game", (request, response) -> {
            try {


                AuthData authData = this.userService.register(user);
                response.status(200);
                return new Gson().toJson(authData);
            }
            catch (UsernameExistsException e){
                response.status(403);
                return new Gson().toJson(Map.of("message", "Error: already taken"));
            }
//            catch (BadRequestException e){
//                response.status(400);
//                return new Gson().toJson(Map.of("message", "Error: bad request"));
//            }
//            catch (FailureException e){
//                response.status(500);
//                return new Gson().toJson(Map.of("message", "Error: description"));
//            }
            //still need 400 and 500 requests. Idk where they come from
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
