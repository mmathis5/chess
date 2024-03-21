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
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.register("user4", "password", "email"));
    }

    @Test
    @Order(2)
    @DisplayName("Failed Register")
    public void failedRegister(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.register("user4", "password", "email"));
    }

    @Test
    @Order(3)
    @DisplayName("Successful Login")
    public void successLogin(){
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.login("user1", "password"));
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
        String authToken = ui.ServerFacade.login("user4", "password");
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.logout(authToken));
    }

    @Test
    @Order(5)
    @DisplayName("Failed Logout")
    public void failLogout() throws Exception {
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.logout("invalidToken"));
    }

}
