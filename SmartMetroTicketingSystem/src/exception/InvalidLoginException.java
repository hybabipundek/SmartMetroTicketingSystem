package exception;

/**
 * Custom exception used when a user provides invalid login credentials.
 */
public class InvalidLoginException extends RuntimeException {

    private static final long serialVersionUID = 1L;

// Initializes the InvalidLoginException object.
    public InvalidLoginException(String message) {
        super(message);
    }

// Initializes the InvalidLoginException object.
    public InvalidLoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
