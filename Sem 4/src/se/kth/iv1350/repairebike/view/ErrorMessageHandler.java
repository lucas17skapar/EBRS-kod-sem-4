package se.kth.iv1350.repairebike.view;

/**
 * Shows user-friendly error messages.
 */
public class ErrorMessageHandler {
    /**
     * Shows the specified error message to the user.
     *
     * @param message The message to show.
     */
    public void showErrorMessage(String message) {
        System.out.println("ERROR: " + message);
    }
}
