package ui;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ClientCommunicator {

    static HttpClient client = HttpClient.newHttpClient();
    static String serviceUrl;
    public static HttpResponse<String> postUser(JsonObject json, String resource) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + resource))
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> postGame(JsonObject json, String resource, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + resource))
                .header("authorization", authToken)
                .POST(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> put(JsonObject json, String resource, String authToken) throws Exception{
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + resource))
                .header("authorization", authToken)
                .PUT(HttpRequest.BodyPublishers.ofString(json.toString()))
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static void delete(String authToken) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + "/session"))
                .header("authorization", authToken)
                .DELETE()
                .build();
        client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    public static HttpResponse<String> get(String resourcs, String authToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(serviceUrl + resourcs))
                .header("authorization", authToken)
                .GET()
                .build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
