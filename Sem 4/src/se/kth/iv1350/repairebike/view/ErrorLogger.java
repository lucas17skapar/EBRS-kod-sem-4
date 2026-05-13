package se.kth.iv1350.repairebike.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 * Writes developer error reports to a file.
 */
public class ErrorLogger {
    private static final String LOG_FILE_NAME = "repair-error-log.txt";

    /**
     * Logs the specified exception to a file.
     *
     * @param exception The exception to log.
     */
    public void logException(Exception exception) {
        try (
            FileWriter fileWriter = new FileWriter(LOG_FILE_NAME, true);
            PrintWriter logStream = new PrintWriter(fileWriter)
        ) {
            logStream.println("Error report written at " + LocalDateTime.now());
            exception.printStackTrace(logStream);
            logStream.println();
        } catch (IOException ioException) {
            System.err.println("Could not write to error log: " + ioException.getMessage());
        }
    }
}
