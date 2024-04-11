package ui;


import chess.ChessBoard;
import chess.ChessPosition;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Scanner;

public class HelperFunctions{
    Scanner scanner = new Scanner(System.in);

    public HelperFunctions(){

    }

    public boolean helperPlayerIsAvailable(String username, String desiredColor, JsonElement jsonElement){
        if (Objects.equals(desiredColor, "WHITE")) {
            JsonElement whiteUserJson = jsonElement.getAsJsonObject().get("whiteUsername");
            try{
                if (Objects.equals(whiteUserJson.toString().substring(1, whiteUserJson.toString().length() - 1), username) || whiteUserJson.isJsonNull()){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                return false;
            }
        }
        else if (Objects.equals(desiredColor, "BLACK")) {
            JsonElement blackUserJson = jsonElement.getAsJsonObject().get("blackUsername");
            try{
                if (Objects.equals(blackUserJson.toString().substring(1, blackUserJson.toString().length() - 1), username) || blackUserJson.isJsonNull()){
                    return true;
                }
                return false;
            }
            catch (Exception e){
                return false;
            }
        }
        return false;
    }

    public ChessPosition helperGetValidCoordinates(String command, ChessBoard chessBoard){
        HashMap<String, Integer> correctColorHashMap;
        //initialize a hash map to get the coordinates in numeric form
        HashMap<String, Integer> letterToNumberWhite = new HashMap<String, Integer>();
        letterToNumberWhite.put("a", 1);
        letterToNumberWhite.put("b", 2);
        letterToNumberWhite.put("c", 3);
        letterToNumberWhite.put("d", 4);
        letterToNumberWhite.put("e", 5);
        letterToNumberWhite.put("f", 6);
        letterToNumberWhite.put("g", 7);
        letterToNumberWhite.put("h", 8);


        ArrayList<Integer> possibleRows = new ArrayList<>();
        possibleRows.add(1);
        possibleRows.add(2);
        possibleRows.add(3);
        possibleRows.add(4);
        possibleRows.add(5);
        possibleRows.add(6);
        possibleRows.add(7);
        possibleRows.add(8);

        //query the client for the piece it wishes to see the moves for
        correctColorHashMap = letterToNumberWhite;


        System.out.println("What is the letter coordinate of the " + command);
        Boolean validLetterCoordinate = false;
        Integer letterCor = 0;
        while (!validLetterCoordinate) {
            String letterCorString = scanner.nextLine().toLowerCase();
            if (correctColorHashMap.containsKey(letterCorString)) {
                validLetterCoordinate = true;
                letterCor = correctColorHashMap.get(letterCorString);
            } else {
                System.out.println("this is an invalid Coordinate. Try Again.");
            }
        }
        System.out.println("What is the number coordinate of " + command);
        Integer numberCor = Integer.parseInt(scanner.nextLine());
        Boolean validNumberCoordinate = false;
        while (!validNumberCoordinate) {
            if (possibleRows.contains(numberCor)) {
                validNumberCoordinate = true;
            } else {
                System.out.println("This is an invalid number coordinate. Enter a valid number now:");
                numberCor = Integer.parseInt(scanner.nextLine());
            }
        }
        return new ChessPosition(numberCor, letterCor);

    }

}
