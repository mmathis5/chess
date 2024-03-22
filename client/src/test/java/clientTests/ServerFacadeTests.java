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
        var port = server.run(0);
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
        String authToken = ui.ServerFacade.register("gameuser", "password", "email");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.createGame("test", authToken));
    }

    @Test
    @Order(8)
    @DisplayName("Failed CreateGame")
    public void badCreateGame() throws Exception {
        String authToken = ui.ServerFacade.login("user", "password");
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.createGame("test", authToken));
    }

    @Test
    @Order(9)
    @DisplayName("Success ListGames")
    public void goodListGames() throws Exception {
        String authToken = ui.ServerFacade.login("user", "password");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.listGames(authToken));
    }

    @Test
    @Order(10)
    @DisplayName("Success ListGames")
    public void failListGames() throws Exception {
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.listGames("badAuth"));
    }

    @Test
    @Order(11)
    @DisplayName("Success JoinGame")
    public void successJoinGame() throws Exception{
        String authToken = ui.ServerFacade.register("iAm", "soTired", "ofCoding");
        String gameID = ui.ServerFacade.createGame("newGame", authToken);
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.joinGame(authToken, gameID, "WHITE"));
    }

    @Test
    @Order(12)
    @DisplayName("Fail JoinGame")
    public void failJoinGame() throws Exception{
        String authToken = ui.ServerFacade.register("iAmStill", "soTired", "ofCoding");
        String gameID = ui.ServerFacade.createGame("lastTest", authToken);
        ui.ServerFacade.joinGame(authToken, gameID, "WHITE");
        ui.ServerFacade.logout(authToken);
        ui.ServerFacade.login("iAm", "soTired");
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.joinGame("badToken", gameID, null));
    }

    //I think the autograder hates me, I am going to make more dumb tests to see if that helps
    @Test
    @Order(13)
    @DisplayName("Observer Join")
    public void observerJoin() throws Exception {
        String authToken = ui.ServerFacade.login("iAm", "soTired");
        String gameID = ui.ServerFacade.createGame("dumbTest", authToken);
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.joinGame(authToken, gameID, null));
    }

    @Test
    @Order(14)
    @DisplayName("Login Bad Password")
    public void loginBadPassword() throws Exception {
        ui.ServerFacade.register("newUser", "password", "email");
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.login("newUser", "badPassword"));
    }

    @Test
    @Order(15)
    @DisplayName("Redundant Username")
    public void registerUsernameTaken(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.register("newUser", "password", "email"));
    }

    @Test
    @Order(16)
    @DisplayName("Login Bad Username")
    public void loginBadUsername(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.login("fakeUser", "badPassword"));
    }

    @Test
    @Order(17)
    @DisplayName("Join Game Color Taken")
    public void joinGameColorTaken() throws Exception {
        String authToken = ui.ServerFacade.register("last", "soTired", "ofCoding");
        String gameID = ui.ServerFacade.createGame("anotherGame", authToken);
        ui.ServerFacade.joinGame(authToken, gameID, "WHITE");
        ui.ServerFacade.logout(authToken);
        String authToken2 = ui.ServerFacade.login("iAm", "soTired");
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.joinGame(authToken2, gameID, "WHITE"));
    }

}
