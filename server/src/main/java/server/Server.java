package server;

import spark.*;
import service.*;
import dataAccess.*;

import javax.xml.crypto.Data;

public class Server {
    private final UserService userService;
    private final GameService gameService;
    private final ClearService clearService;

    public Server(){
        this.userService = new UserService();
        this.gameService = new GameService();
        this.clearService = new ClearService();
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        Spark.delete("/db", (request, response) -> {
            response.status(200);
            //Call the ClearService
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
