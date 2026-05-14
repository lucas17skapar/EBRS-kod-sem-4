package se.kth.iv1350.repairebike.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.controller.Controller;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewTest {
    private StringWriter userMessages;
    private StringWriter errorLog;
    private View view;

    @BeforeEach
    void setUp() {
        Controller controller = new Controller(
            CustomerRegistry.getInstance(),
            new RepairOrderRegistry(),
            new Printer()
        );
        userMessages = new StringWriter();
        errorLog = new StringWriter();
        view = new View(
            controller,
            new ErrorMessageHandler(new PrintWriter(userMessages)),
            new ErrorLogger(new PrintWriter(errorLog))
        );
    }

    @Test
    void findCustomerShowsFriendlyMessageForMissingCustomer() {
        assertNull(view.findCustomer("0000000000"));

        assertTrue(userMessages.toString().contains("No customer was found with phone number 0000000000."));
        assertTrue(errorLog.toString().isEmpty());
    }

    @Test
    void findCustomerLogsOperationFailedException() {
        assertNull(view.findCustomer(CustomerRegistry.DATABASE_FAILURE_PHONE_NUMBER));

        assertTrue(userMessages.toString().contains("Please try again later."));
        assertTrue(errorLog.toString().contains("OperationFailedException"));
        assertTrue(errorLog.toString().contains("DatabaseFailureException"));
    }
}
