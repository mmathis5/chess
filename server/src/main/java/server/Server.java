package server;

import spark.*;
import service.*;
import dataAccess.*;

import javax.xml.crypto.Data;

public class Server {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    private final UserDAO userDAO = new MemoryUserDAO();
    private final AuthDAO authDAO = new MemoryAuthDAO();
    private final GameDAO gameDAO = new MemoryGameDAO();

    public Server(){
        this.userService = new UserService(userDAO);
        this.gameService = new GameService(gameDAO);
        this.clearService = new ClearService(userDAO, gameDAO, authDAO);
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", (request, response) -> {
            clearService.clear();
            response.status(200);
            return "";
        });

        // Register your endpoints and handle exceptions here.


        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
