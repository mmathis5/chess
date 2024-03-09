package dataAccessTests;

import dataAccess.*;
import dataAccess.exceptions.DataAccessException;
import dataAccess.exceptions.InternalFailureException;
import dataAccess.exceptions.UsernameExistsException;
import model.*;
import org.junit.jupiter.api.*;

import java.sql.SQLException;

public class DAOTests {
    private final UserDAO userDAO;
    {
        try {
            userDAO = new SQLUserDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final AuthDAO authDAO;

    {
        try {
            authDAO = new SQLAuthDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private final GameDAO gameDAO;

    {
        try {
            gameDAO = new SQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public DAOTests(){}

    @Test
    @Order(1)
    @DisplayName("User Clear")
    public void userClear(){
        Assertions.assertDoesNotThrow(userDAO::clear);
    }

    @Test
    @Order(2)
    @DisplayName("Game Clear")
    public void authClear(){
        Assertions.assertDoesNotThrow(authDAO::clear);
    }
    @Test
    @Order(3)
    @DisplayName("Auth Clear")
    public void gameClear(){
        Assertions.assertDoesNotThrow(gameDAO::clear);
    }


    @Test
    @Order(4)
    @DisplayName("Add User")
    public void addUserPass(){
        String username = "user";
        String password = "password";
        String email = "email";
        UserData userData = new UserData(username, password, email);
        Assertions.assertDoesNotThrow(() -> userDAO.addUser(username, userData));
    }
    @Test
    @Order(5)
    @DisplayName("Username Taken")
    public void usernameTaken() throws SQLException, UsernameExistsException {
        String username = "usertest";
        String password = "password2";
        String email = "email1";
        UserData userData = new UserData(username, password, email);
        userDAO.addUser(username, userData);
        Assertions.assertThrows(UsernameExistsException.class, () -> userDAO.addUser(username, userData));
    }

    @Test
    @Order(6)
    @DisplayName("Get User")
    public void getUserPass(){
        Assertions.assertDoesNotThrow(() -> userDAO.getUser("user"));
    }
    @Test
    @Order(7)
    @DisplayName("Get User Fail")
    public void getUserFail() throws SQLException {
        Assertions.assertEquals(null, userDAO.getUser("invalidUsername"));
    }

    @Test
    @Order(8)
    @DisplayName("User Exists True")
    public void userExists() throws SQLException, UsernameExistsException {
        String username = "userExistsTest";
        String password = "password2";
        String email = "email1";
        UserData userData = new UserData(username, password, email);
        userDAO.addUser(username, userData);
        Assertions.assertEquals(Boolean.TRUE, userDAO.userExists(username));
    }

    @Test
    @Order(9)
    @DisplayName("User Exists False")
    public void userExistsFalse(){
        Assertions.assertEquals(Boolean.FALSE, userDAO.userExists("invalidUsername"));
    }

    //AuthDAO Tests
    @Test
    @Order(10)
    @DisplayName("Add AuthData Pass")
    public void addAuthDataPass(){
        String authToken = authDAO.generateAuthToken();
        AuthData authData = new AuthData(authToken, "user");
        Assertions.assertDoesNotThrow(() -> authDAO.addAuthData(authToken, authData));
    }

    @Test
    @Order(11)
    @DisplayName("Add AuthData Fail")
    public void addAuthDataFail() throws SQLException, DataAccessException {
        String authToken = "token";
        AuthData authData = new AuthData(authToken, "user");
        authDAO.addAuthData(authToken, authData);
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.addAuthData(authToken, authData));
    }

    @Test
    @Order(12)
    @DisplayName("Get AuthData Present")
    public void getAuthDataPresent() throws SQLException, DataAccessException {
        String authToken = authDAO.generateAuthToken();
        AuthData authData = new AuthData(authToken, "user");
        authDAO.addAuthData(authToken, authData);
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth(authToken));
    }

    @Test
    @Order(13)
    @DisplayName("Get AuthData Null")
    public void getAuthDataNull() throws SQLException, DataAccessException {
        Assertions.assertNull(authDAO.getAuth("fakeAuthToken"));
    }

    @Test
    @Order(14)
    @DisplayName("Delete Auth")
    public void deleteAuthPass() throws SQLException, DataAccessException {
        String authToken = authDAO.generateAuthToken();
        AuthData authData = new AuthData(authToken, "user");
        authDAO.addAuthData(authToken, authData);
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth(authToken));
    }

    @Test
    @Order(15)
    @DisplayName("Delete Auth Fail")
    public void deleteAuthFail() throws SQLException, DataAccessException {
        String authToken = authDAO.generateAuthToken();
        AuthData authData = new AuthData(authToken, "user");
        authDAO.addAuthData(authToken, authData);
        Assertions.assertThrows(Exception.class, () -> authDAO.deleteAuth("fake token"));
    }

    @Test
    @Order(16)
    @DisplayName("Create Game")
    public void createGamePass() throws SQLException, DataAccessException {
        String authToken = authDAO.generateAuthToken();
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame(authToken, "testGame"));
    }

    @Test
    @Order(17)
    @DisplayName("Create Game Fail")
    public void createGameFail() throws SQLException, DataAccessException {
        String authToken = authDAO.generateAuthToken();
        AuthData authData = new AuthData(authToken, "user");
        authDAO.addAuthData(authToken, authData);
        Assertions.assertThrows(Exception.class, () -> authDAO.deleteAuth("fake token"));
    }

    @Test
    @Order(18)
    @DisplayName("Get Game Pass")
    public void getGamePass() throws SQLException, DataAccessException {

    }

    @Test
    @Order(19)
    @DisplayName("Get Game Fail")
    public void getGameFail() throws SQLException, DataAccessException {

    }

    @Test
    @Order(20)
    @DisplayName("Get Game List Pass")
    public void getGameListPass() throws SQLException, DataAccessException {

    }
    @Test
    @Order(21)
    @DisplayName("Get Game List Fail")
    public void getGameListFail() throws SQLException, DataAccessException {

    }

    @Test
    @Order(22)
    @DisplayName("Update Game Pass")
    public void updateGamePass() throws SQLException, DataAccessException {

    }
    @Test
    @Order(23)
    @DisplayName("Update Game Fail")
    public void updateGameFail() throws SQLException, DataAccessException {

    }



}
