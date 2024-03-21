package test.clientTests;

import dataAccess.exceptions.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import ui.*;


public class ServerFacadeTests {

    private static Server server;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
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
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.register("user3", "password", "email"));
    }

    @Test
    @Order(2)
    @DisplayName("Successful Login")
    public void successLogin(){
        Assertions.assertDoesNotThrow(() -> ui.ServerFacade.login("user1", "password"));
    }

    @Test
    @Order(3)
    @DisplayName("Fail Login")
    public void failLogin(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.login("fakeuser", "password"));
    }

    @Test
    @Order(2)
    @DisplayName("Failed Login")
    public void failedLogin(){
        Assertions.assertThrows(Exception.class, () -> ui.ServerFacade.login("user", "password"));
    }
}
