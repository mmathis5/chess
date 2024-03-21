package ui;
import com.sun.nio.sctp.NotificationHandler;
import server.Server;
import ui.ChessBoardUI;
import ui.ServerFacade;
import ui.ClientCommunicator;

import java.io.Console;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Client {
    String authToken = null;
    Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Server server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);

        Client client = new Client();
        try {
            client.preLogin();
        }
        finally {
            server.stop();
        }
    }


    public void preLogin(){
        while (this.authToken == null) {
            System.out.println("1. Help");
            System.out.println("2. Quit");
            System.out.println("3. Login");
            System.out.println("4. Register");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            if (Objects.equals(input, "2")) {
                break;
            }
            evalPreLogin(input);
        }
        postLogin();
    }

    public void postLogin() {
        while (this.authToken != null){
            System.out.println("1. Help");
            System.out.println("2. Logout");
            System.out.println("3. Create Game");
            System.out.println("4. List Games");
            System.out.println("5. Join Game");
            System.out.println("6. Join Observer");
            System.out.println("Enter Command: ");
            String input = scanner.nextLine();
            if (Objects.equals(input, "2")) {
                logout();
                break;
            }
            evalPostLogin(input);
        }
        preLogin();
    }

    public void evalPostLogin(String input) {
        try {
            switch (input) {
                case "1" -> helpPostLogin();
                case "2" -> logout();
                case "3" -> createGame();
                case "4" -> listGames();
                case "5" -> joinGame();
                case "6" -> joinObserver();
                default -> System.out.println("Unknown Command Number");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void evalPreLogin(String input) {
        try {
            switch (input) {
                case "1" -> helpPreLogin();
                case "3" -> login();
                case "4" -> register();
                default -> System.out.println("Unknown Command Number");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void helpPreLogin(){
        System.out.println("Help: If you have a user account, enter 3 to log in. If not, enter 4 to register.");
    }
    public void helpPostLogin(){
        System.out.println("Help: Type a number to execute a command in game mode");
    }

    public void login(){
        try{
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            this.authToken = ServerFacade.login(username, password);
        }catch (Exception e){
            System.out.println("Error during login" + e.getMessage());
        }
    }
    public void register(){
        try{
            System.out.println("Enter Username:");
            String username = scanner.nextLine();
            System.out.println("Enter Password:");
            String password = scanner.nextLine();
            System.out.println("Enter Email: ");
            String email = scanner.nextLine();
            this.authToken = ServerFacade.register(username, password, email);
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void logout(){
        try{
            ServerFacade.logout(this.authToken);
            this.authToken = null;
        }
        catch (Exception e){
            System.out.println("Error: "+ e.getMessage());
        }

    }
    public void createGame(){
        System.out.println("Enter Game Name: ");
        String gameName = scanner.nextLine();
        try{
            String gameID = ServerFacade.createGame(gameName, this.authToken);
            System.out.println("GameID: " + gameID);
        }
        catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void listGames(){
        try{
            ui.ServerFacade.listGames(this.authToken);
        }
        catch(Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void joinGame(){

    }

    public void joinObserver(){

    }


}
