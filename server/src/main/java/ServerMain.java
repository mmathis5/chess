import dataAccess.exceptions.DataAccessException;
import server.Server;

import java.sql.SQLException;

public class ServerMain {
    public static void main(String[] args) throws SQLException, DataAccessException {
        Server server = new Server();
        server.run(8080);
        //WSServer wSServer = new WSServer();
        //wSServer.run(8081);
    }
}