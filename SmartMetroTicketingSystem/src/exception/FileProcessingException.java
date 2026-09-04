package exception;

/**
 * Custom exception used to report errors that occur while saving or loading application data.
 */
public class FileProcessingException extends RuntimeException {

    private static final long serialVersionUID = 1L;

// Initializes the FileProcessingException object.
    public FileProcessingException(String message) {
        super(message);
    }

// Initializes the FileProcessingException object.
    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
