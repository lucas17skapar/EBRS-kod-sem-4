package se.kth.iv1350.repairebike.controller;

/**
 * Thrown when the application can not complete an operation.
 */
public class OperationFailedException extends Exception {
    /**
     * Creates a new exception for a failed operation.
     *
     * @param message A message explaining the failed operation.
     * @param cause The lower-level exception that caused this exception.
     */
    public OperationFailedException(String message, Exception cause) {
        super(message, cause);
    }
}
