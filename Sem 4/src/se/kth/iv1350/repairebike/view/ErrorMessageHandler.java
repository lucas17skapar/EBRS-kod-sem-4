package se.kth.iv1350.repairebike.view;

import java.io.PrintWriter;

/**
 * Shows user-friendly error messages.
 */
public class ErrorMessageHandler {
    private final PrintWriter messageStream;

    /**
     * Creates an error message handler that writes to standard output.
     */
    public ErrorMessageHandler() {
        this(new PrintWriter(System.out, true));
    }

    /**
     * Creates an error message handler that writes to the specified stream.
     *
     * @param messageStream The stream to write messages to.
     */
    public ErrorMessageHandler(PrintWriter messageStream) {
        this.messageStream = messageStream;
    }

    /**
     * Shows the specified error message to the user.
     *
     * @param message The message to show.
     */
    public void showErrorMessage(String message) {
        messageStream.println("ERROR: " + message);
        messageStream.flush();
    }
}
