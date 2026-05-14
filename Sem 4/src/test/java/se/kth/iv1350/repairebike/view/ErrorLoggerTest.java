package se.kth.iv1350.repairebike.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.controller.OperationFailedException;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ErrorLoggerTest {
    @Test
    void logExceptionWritesErrorReportAndExceptionSummary() {
        StringWriter log = new StringWriter();
        ErrorLogger errorLogger = new ErrorLogger(new PrintWriter(log));
        OperationFailedException exception = new OperationFailedException(
            "Could not search for customer.",
            new Exception("Database down.")
        );

        errorLogger.logException(exception);

        assertTrue(log.toString().contains("Error report written at"));
        assertTrue(log.toString().contains("OperationFailedException"));
        assertTrue(log.toString().contains("Database down."));
    }
}
