package exception;

/**
 * Custom exception used when a requested ticket cannot be found.
 */
public class TicketNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

// Initializes the TicketNotFoundException object.
    public TicketNotFoundException(String message) {
        super(message);
    }

// Initializes the TicketNotFoundException object.
    public TicketNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
