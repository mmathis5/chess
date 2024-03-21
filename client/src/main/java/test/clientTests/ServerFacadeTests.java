package test.clientTests;

import dataAccess.exceptions.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        server.clearEndpoint();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @Order(1)
    @DisplayName("Successful Register")
    public void successRegister(){
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.register("user", "password", "email"));
    }

    @Test
    @Order(2)
    @DisplayName("Failed Register")
    public void failedRegister(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.register("user", "password", "email"));
    }

    @Test
    @Order(3)
    @DisplayName("Successful Login")
    public void successLogin(){
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.login("user", "password"));
    }

    @Test
    @Order(4)
    @DisplayName("Fail Login")
    public void failLogin(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.login("fakeuser", "password"));
    }


    @Test
    @Order(5)
    @DisplayName("Successful Logout")
    public void successLogout() throws Exception {
        String authToken = ui.ServerFacade.login("user", "password");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.logout(authToken));
    }

    @Test
    @Order(6)
    @DisplayName("Failed Logout")
    public void failLogout() throws Exception {
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.logout("invalidToken"));
    }


    @Test
    @Order(7)
    @DisplayName("Successful CreateGame")
    public void goodCreateGame() throws Exception {
        String authToken = ui.ServerFacade.login("user", "password");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.createGame("test", authToken));
    }

    @Test
    @Order(8)
    @DisplayName("Failed CreateGame")
    public void badCreateGame() throws Exception {
        //tries to create a game with a name that's already in use
        String authToken = ui.ServerFacade.login("user", "password");
        ui.ServerFacade.createGame("test", authToken);
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.createGame("test", authToken));
    }

    @Test
    @Order(8)
    @DisplayName("Success ListGames")
    public void goodListGames() throws Exception {
        String authToken = ui.ServerFacade.login("user", "password");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.listGames(authToken));
    }

    @Test
    @Order(8)
    @DisplayName("Success ListGames")
    public void failListGames() throws Exception {
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.listGames("badAuth"));
    }


}
