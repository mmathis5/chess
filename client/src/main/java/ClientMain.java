import ui.Client;

public class ClientMain {
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        Client client = new Client(serverURL);
        client.preLogin();

    }
}