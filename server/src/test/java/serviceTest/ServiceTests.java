package serviceTest;

import dataAccess.exceptions.*;
import dataAccess.*;
import org.junit.jupiter.api.*;
import service.ClearService;
import service.GameService;
import service.UserService;
import model.*;

import java.io.IOException;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceTests {
  private final String username = "user";
  private final String password = "user1";
  private final String email = "user@byu.edu";
  static String authToken;
  static String gameID;
  AuthDAO authDAO = new MemoryAuthDAO();
  UserDAO userDAO = new MemoryUserDAO();
  GameDAO gameDAO = new MemoryGameDAO();
  UserService userService = new UserService(userDAO, authDAO);
  GameService gameService = new GameService(gameDAO, authDAO);
  ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);

  @Test
  @Order(1)
  @DisplayName("Successful Registration")
  public void successRegister() throws DataAccessException, UsernameExistsException, InternalFailureException {
    UserData user = new UserData(username, password, email);
    var auth = userService.register(user);
    Assertions.assertNotNull(auth, "The authToken that registration returned should not be null");
  }

  @Test
  @Order(2)
  @DisplayName("Username Taken")
  public void usernameTaken() throws DataAccessException, UsernameExistsException, InternalFailureException {
    UserData user = new UserData(username, password, email);
    var auth = userService.register(user);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.register(user);
    }, "The username is taken already");
  }

  @Test
  @Order(3)
  @DisplayName("Bad Request")
  public void badRequest() throws DataAccessException, UsernameExistsException, InternalFailureException {
    UserData user = new UserData(null, password, email);
    var auth = userService.register(user);
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.register(user);
    }, "Register with a null name is a bad request");
  }

  @Test
  @Order(4)
  @DisplayName("Bad Request")
  public void internalError() throws DataAccessException, UsernameExistsException, InternalFailureException {
    UserData user = new UserData(null, password, email);
    var auth = userService.register(user);
    Assertions.assertThrows(Exception.class, () -> {
      userService.register(user);
    }, "Register with a null name is a bad request");
  }


  @Order(5)
  @Test
  public void successLogin() throws DataAccessException, InternalFailureException, UsernameExistsException {
    //register a user then log them in
    UserData user = new UserData("Maddie", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie", "1");
    Assertions.assertNotNull(authData, "The authToken that login returned should not be null");
  }
  @Test
  @Order(6)
  @DisplayName("Failed Login")
  public void failedLogin() throws DataAccessException, InternalFailureException, UsernameExistsException {
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.login("James", "1");
    }, "This user isn't valid");
  }

  @Test
  @Order(7)
  @DisplayName("Succesful Logout")
  public void succesfulLogout() throws DataAccessException, InternalFailureException, UsernameExistsException {
    UserData user = new UserData("Maddie", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie", "1");
    //get the authToken
    String authToken = authData.getAuthToken();
    Assertions.assertDoesNotThrow(() -> userService.logout(authToken));
  }

  @Test
  @Order(8)
  @DisplayName("FailedLogout")
  public void failLogout() {
    String fakeAuth = "fakeAuth";
    Assertions.assertThrows(DataAccessException.class, () -> {
      userService.logout(fakeAuth);
    }, "Incorrect authToken should throw an error");
  }

  @Test
  @Order(9)
  public void successCreateGame() throws DataAccessException, InternalFailureException, UsernameExistsException {
    UserData user = new UserData("Maddie1", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie1", "1");
    String authToken = authData.getAuthToken();
    Assertions.assertDoesNotThrow(() -> gameService.createGame(authToken, "gameName"));
  }

  @Test
  @Order(10)
  @DisplayName("Failed Game Creation")
  public void failCreateGame() {
    String fakeAuth = "fakeAuth";
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.createGame(fakeAuth, "badName");
    }, "Incorrect authToken should throw an error");
  }

  @Test
  @Order(11)
  public void successJoinGame() throws DataAccessException, InternalFailureException, UsernameExistsException {
    UserData user = new UserData("Maddie1", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie1", "1");
    String authToken = authData.getAuthToken();
    Integer gameID = gameService.createGame(authToken, "gameName");
    Assertions.assertDoesNotThrow(() -> gameService.joinGame(gameID, "WHITE", authToken));
  }

  @Test
  @Order(12)
  @DisplayName("Failed Game Join")
  public void failJoinGame() {
    String fakeAuth = "fakeAuth";
    Integer fakeGameID = 33;
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.joinGame(fakeGameID, null, fakeAuth);
    }, "Incorrect authToken should throw an error");
  }

  @Test
  @Order(13)
  @DisplayName("Successful List Game")
  public void succesfulListGame() throws DataAccessException, InternalFailureException, UsernameExistsException {
    UserData user = new UserData("Maddie1", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie1", "1");
    String authToken = authData.getAuthToken();
    gameService.createGame(authToken, "gameName");
    gameService.createGame(authToken, "gameName2");
    Assertions.assertDoesNotThrow(() -> gameService.listGames(authToken));
  }

  @Test
  @Order(14)
  @DisplayName("Failed List Games")
  public void failedListGame(){
    String fakeAuth = "fakeAuth";
    Assertions.assertThrows(DataAccessException.class, () -> {
      gameService.createGame(fakeAuth, "badName");
    }, "Incorrect authToken should throw an error");
  }

  @Order(15)
  @Test
  @DisplayName("Successful Clear")
  public void successClearApplication() throws DataAccessException, UsernameExistsException, InternalFailureException, SQLException, IOException, ClassNotFoundException {
    UserData user = new UserData("Maddie1", "1", "a@gmail.com");
    userService.register(user);
    AuthData authData = userService.login("Maddie1", "1");
    String authToken = authData.getAuthToken();
    int gameID = gameService.createGame(authToken, "gameName");
    gameService.createGame(authToken, "gameName2");
    clearService.clear();
    Assertions.assertThrows(Exception.class, () -> {
      userService.login("Maddie1", "1");
    }, "No userdata in the database");
    assertNull(authDAO.getAuth(authToken));
    assertNull(gameDAO.getGame(gameID));
  }

}