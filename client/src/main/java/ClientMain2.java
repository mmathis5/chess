import ui.Client;

public class ClientMain2 {
    public static void main(String[] args) {
        var serverURL = "http://localhost:8080";
        Client client = new Client(serverURL);
        client.preLogin();

    }
}