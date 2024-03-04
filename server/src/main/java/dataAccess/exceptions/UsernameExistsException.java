package dataAccess.exceptions;

/**
 * Indicates the username already exists
 */
public class UsernameExistsException extends Exception{
    public UsernameExistsException(String message) {
        super(message);
    }
}
