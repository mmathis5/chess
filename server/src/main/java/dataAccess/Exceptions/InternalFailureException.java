package dataAccess.Exceptions;

/**
 * Indicates that something went wrong internally.
 */
public class InternalFailureException extends Exception{
    public InternalFailureException(String message) {
        super(message);
    }
}
