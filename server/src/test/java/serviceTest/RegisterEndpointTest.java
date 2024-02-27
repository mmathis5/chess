package serviceTest;

import dataAccess.AuthDAO;
import dataAccess.UserDAO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import service.*;
import model.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RegisterEndpointTest {
    private UserDAO userDAO;
    private AuthDAO authDAO;
    @Test
    @Order(0)
    @DisplayName("Successful Registration")
    public void successfulRegistration(){
        UserService userService = new UserService(userDAO, authDAO);
        UserData user = new UserData("username1", "password1", "email1@hotamil.com");
        assertDoesNotThrow(() -> {
            AuthData authData = userService.register(user);
            assertNotNull(authData);
        });
    }

    //username taken 403
    //bad request 400

}
