package ui;
import com.google.gson.JsonObject;
import ui.ServerFacade;
import ui.ChessBoardUI;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.google.gson.JsonObject;

public class ClientCommunicator {

    static HttpClient client = HttpClient.newHttpClient();
    static String serviceUrl = "http://localhost:8080";
    public static HttpResponse<String> post(JsonObject json, String resource) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + resource))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response;
    }
}
