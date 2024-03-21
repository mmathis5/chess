package ui;

import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ServerFacade {
    static HttpClient client = HttpClient.newHttpClient();
    static String serviceUrl = "http://localhost:8080";
    public ServerFacade(int port){

    }
    public static String login(String username, String password) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        try {
            HttpResponse<String> response = ClientCommunicator.postUser(json, "/session");
            if (response.statusCode() == 200) {
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                return jsonObject.get("authToken").getAsString();
            } else {
                throw new Exception("error during login " + response.statusCode());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static String register(String username, String password, String email) throws Exception {
        JsonObject json = new JsonObject();
        json.addProperty("username", username);
        json.addProperty("password", password);
        json.addProperty("email", email);
        try {
            HttpResponse<String> response = ClientCommunicator.postUser(json, "/user");
            if (response.statusCode() == 200) {
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                return jsonObject.get("authToken").getAsString();
            }
            else if (response.statusCode() == 403) {
                throw new Exception("Username Already Taken");
            }
            else{
                throw new Exception("Unknown Error " + response.statusCode());
            }
        } catch (Exception e) {
            throw e;
        }
    }

    public static void logout(String authToken) throws Exception {
        try{
           ClientCommunicator.delete(authToken);
        }
        catch (Exception e){
            throw e;
        }
    }

    public static String createGame(String gameName, String authToken) throws Exception {
        try{
            JsonObject json = new JsonObject();
            json.addProperty("gameName", gameName);
            HttpResponse<String> response = ClientCommunicator.postGame(json, "/game", authToken);
            if (response.statusCode() == 200) {
                JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                return jsonObject.get("gameID").getAsString();
            }
            else if (response.statusCode() == 403) {
                throw new Exception("Game Name Already Taken");
            }
            else{
                throw new Exception("Unknown Error " + response.statusCode());
            }
        }
        catch(Exception e){
            throw e;
        }
    }

    public static void listGames(String authToken) throws Exception{
        try{
            HttpResponse<String> response = ui.ClientCommunicator.get("/game", authToken);
        }
        catch(Exception e){
            throw e;
        }
    }
}
