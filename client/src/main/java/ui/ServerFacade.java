package ui;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;

import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ServerFacade {
    static HttpClient client = HttpClient.newHttpClient();
    static String serviceUrl;

    public ServerFacade(String serviceUrl){
        ClientCommunicator.serviceUrl = serviceUrl;
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
            } else if (response.statusCode() == 401){
                throw new Exception("The entered username or password is invalid\n");
            }
            else {
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
            else if (response.statusCode() == 401) {
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

    public static JsonArray listGames(String authToken) throws Exception{
        try{
            HttpResponse<String> response = ui.ClientCommunicator.get("/game", authToken);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            return jsonObject.get("games").getAsJsonArray();

        }
        catch(Exception e){
            throw e;
        }
    }

    public static void joinGame(String authToken, String gameID, String playerColor) throws Exception {
        try{
            JsonObject json = new JsonObject();
            json.addProperty("gameID", gameID);
            if (playerColor != null){
                json.addProperty("playerColor", playerColor);
            }
            HttpResponse response = ClientCommunicator.put(json, "/game", authToken);
            if (response.statusCode() != 200){
                throw new Exception("Something went wrong while you were joining");
            }
        }
        catch(Exception e){
            throw e;
        }
    }
}
