package dataAccess.exceptions;

/**
 * Indicates there was an error connecting to the database
 */
public class GameNameTakenException extends Exception{
    public GameNameTakenException(String message) {
        super(message);
    }
}
