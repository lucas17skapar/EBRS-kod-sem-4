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
    private final PrintWriter logStream;

    /**
     * Creates an error logger that writes to the default log file.
     */
    public ErrorLogger() {
        this(null);
    }

    /**
     * Creates an error logger that writes to the specified stream.
     *
     * @param logStream The stream to write to.
     */
    public ErrorLogger(PrintWriter logStream) {
        this.logStream = logStream;
    }

    /**
     * Logs the specified exception to a file.
     *
     * @param exception The exception to log.
     */
    public void logException(Exception exception) {
        if (logStream != null) {
            writeException(exception, logStream);
            logStream.flush();
            return;
        }

        try (
            FileWriter fileWriter = new FileWriter(LOG_FILE_NAME, true);
            PrintWriter fileLogStream = new PrintWriter(fileWriter)
        ) {
            writeException(exception, fileLogStream);
        } catch (IOException ioException) {
            System.err.println("Could not write to error log: " + ioException.getMessage());
        }
    }

    private void writeException(Exception exception, PrintWriter stream) {
        stream.println("Error report written at " + LocalDateTime.now());
        exception.printStackTrace(stream);
        stream.println();
    }
}
