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

    public void postLogin() {
        System.out.println("1. Help");
        System.out.println("2. Quit");
        System.out.println("3. Login");
        System.out.println("4. Register");
        System.out.println("Enter Command: ");


        preLogin();
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

    public void evalPreLogin(String input) {
        try {
            switch (input) {
                case "1" -> help();
                case "3" -> login();
                case "4" -> register();
                default -> System.out.println("Unknown Command Number");
            };
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void help(){
        System.out.println("If you have a user account, enter 3 to log in. If not, enter 4 to register.");
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


}
