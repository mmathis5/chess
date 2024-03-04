package dataAccess.exceptions;

/**
 * Indicates that something went wrong internally.
 */
public class JoinGameColorException extends Exception{
    public JoinGameColorException(String message) {
        super(message);
    }
}
