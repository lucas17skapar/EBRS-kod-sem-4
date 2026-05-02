package se.kth.iv1350.repairebike.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairebike.model.Amount;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairOrderState;
import se.kth.iv1350.repairebike.model.RepairTask;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class ControllerTest {
    private Controller controller;
    private SilentPrinter printer;

    @BeforeEach
    void setUp() {
        CustomerRegistry customerRegistry = new CustomerRegistry();
        RepairOrderRegistry repairOrderRegistry = new RepairOrderRegistry();
        printer = new SilentPrinter();
        controller = new Controller(customerRegistry, repairOrderRegistry, printer);
    }

    @Test
    void findCustomerReturnsCorrectCustomerForKnownPhoneNumber() {
        Customer result = controller.findCustomer("0701234567");

        assertNotNull(result);
        assertEquals("Sara Lind", result.getName());
        assertEquals("0701234567", result.getPhoneNumber());
    }

    @Test
    void findCustomerReturnsNullForUnknownPhoneNumber() {
        Customer result = controller.findCustomer("0000000000");

        assertNull(result);
    }

    @Test
    void createRepairOrderReturnsNullWhenNoCustomerHasBeenSelected() {
        RepairOrder result = controller.createRepairOrder("Battery drains quickly.");

        assertNull(result);
    }

    @Test
    void createRepairOrderCreatesAndRegistersRepairOrderWhenCustomerExists() {
        controller.findCustomer("0701234567");

        RepairOrder createdRepairOrder = controller.createRepairOrder("Battery drains quickly.");

        assertNotNull(createdRepairOrder);
        RepairOrder registeredRepairOrder = controller.findRepairOrder(createdRepairOrder.getOrderId());
        assertSame(createdRepairOrder, registeredRepairOrder);
        assertEquals(1, registeredRepairOrder.getOrderId());
        assertEquals("Battery drains quickly.", registeredRepairOrder.getProblemDescription());
    }

    @Test
    void createRepairOrderCreatesUniqueRepairOrderIds() {
        controller.findCustomer("0701234567");
        RepairOrder firstOrder = controller.createRepairOrder("First issue.");
        RepairOrder secondOrder = controller.createRepairOrder("Second issue.");

        assertNotNull(firstOrder);
        assertNotNull(secondOrder);
        RepairOrder firstResult = controller.findRepairOrder(firstOrder.getOrderId());
        RepairOrder secondResult = controller.findRepairOrder(secondOrder.getOrderId());
        assertSame(firstOrder, firstResult);
        assertSame(secondOrder, secondResult);
        assertEquals(1, firstOrder.getOrderId());
        assertEquals(2, secondOrder.getOrderId());
    }

    @Test
    void findRepairOrderReturnsOrderWithMatchingId() {
        controller.findCustomer("0701234567");
        RepairOrder firstOrder = controller.createRepairOrder("First issue.");
        RepairOrder secondOrder = controller.createRepairOrder("Second issue.");

        RepairOrder result = controller.findRepairOrder(firstOrder.getOrderId());

        assertNotNull(secondOrder);
        assertSame(firstOrder, result);
    }

    @Test
    void addDiagnosticReportReturnsNullWhenNoRepairOrderMatchesId() {
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        RepairOrder result = controller.addDiagnosticReport(999, "Connector issue.", tasks);

        assertNull(result);
    }

    @Test
    void addDiagnosticReportUpdatesSpecifiedRepairOrderCorrectly() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        tasks.add(new RepairTask("Update firmware", new Amount(650.0)));

        RepairOrder updatedOrder = controller.addDiagnosticReport(
            createdOrder.getOrderId(),
            "Connector and firmware issues.",
            tasks
        );

        assertNotNull(updatedOrder);
        assertEquals("Connector and firmware issues.", updatedOrder.getDiagnosticReport().getReportText());
        assertEquals(2, updatedOrder.getRepairTasks().size());
        assertEquals(new Amount(1550.0), updatedOrder.calculateTotalCost());
        assertEquals(RepairOrderState.NEWLY_CREATED, updatedOrder.getState());
        assertSame(updatedOrder, controller.findRepairOrder(createdOrder.getOrderId()));
    }

    @Test
    void addDiagnosticReportUpdatesSpecifiedRepairOrderInsteadOfLatestOrder() {
        controller.findCustomer("0701234567");
        RepairOrder firstOrder = controller.createRepairOrder("First issue.");
        RepairOrder secondOrder = controller.createRepairOrder("Second issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        RepairOrder updatedOrder = controller.addDiagnosticReport(
            firstOrder.getOrderId(),
            "Connector issue.",
            tasks
        );

        assertSame(firstOrder, updatedOrder);
        assertEquals("Connector issue.", firstOrder.getDiagnosticReport().getReportText());
        assertNull(secondOrder.getDiagnosticReport());
        assertSame(secondOrder, controller.findRepairOrder(secondOrder.getOrderId()));
    }

    @Test
    void addDiagnosticReportStoresSpecifiedEstimatedCompletionDate() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        LocalDate expectedCompletionDate = createdOrder.getCreatedDate().plusDays(5);

        RepairOrder updatedOrder = controller.addDiagnosticReport(
            createdOrder.getOrderId(),
            "Connector issue.",
            tasks,
            expectedCompletionDate
        );

        assertNotNull(updatedOrder);
        assertEquals(expectedCompletionDate, updatedOrder.getEstimatedCompletionDate());
    }

    @Test
    void prepareRepairOrderForApprovalReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrder result = controller.prepareRepairOrderForApproval(999);

        assertNull(result);
    }

    @Test
    void prepareRepairOrderForApprovalChangesSpecifiedOrderStateWhenDiagnosticExists() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);

        RepairOrder preparedOrder = controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        assertNotNull(preparedOrder);
        assertEquals(RepairOrderState.READY_FOR_APPROVAL, preparedOrder.getState());
        assertSame(preparedOrder, controller.findRepairOrder(createdOrder.getOrderId()));
    }

    @Test
    void acceptRepairOrderReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrder result = controller.acceptRepairOrder(999);

        assertNull(result);
    }

    @Test
    void acceptRepairOrderChangesStateToAcceptedWhenSpecifiedRepairOrderIsReadyForApproval() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);
        controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        RepairOrder acceptedOrder = controller.acceptRepairOrder(createdOrder.getOrderId());

        assertNotNull(acceptedOrder);
        assertEquals(RepairOrderState.ACCEPTED, acceptedOrder.getState());
        assertSame(acceptedOrder, controller.findRepairOrder(createdOrder.getOrderId()));
        assertSame(acceptedOrder, printer.getPrintedRepairOrder());
    }

    @Test
    void acceptRepairOrderDoesNotAcceptOrderBeforeItIsReadyForApproval() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");

        RepairOrder acceptedOrder = controller.acceptRepairOrder(createdOrder.getOrderId());

        assertNotNull(acceptedOrder);
        assertEquals(RepairOrderState.NEWLY_CREATED, acceptedOrder.getState());
        assertNull(printer.getPrintedRepairOrder());
    }

    @Test
    void rejectRepairOrderReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrder result = controller.rejectRepairOrder(999);

        assertNull(result);
    }

    @Test
    void rejectRepairOrderChangesStateToRejectedWhenSpecifiedRepairOrderIsReadyForApproval() {
        controller.findCustomer("0701234567");
        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);
        controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        RepairOrder rejectedOrder = controller.rejectRepairOrder(createdOrder.getOrderId());

        assertNotNull(rejectedOrder);
        assertEquals(RepairOrderState.REJECTED, rejectedOrder.getState());
        assertSame(rejectedOrder, controller.findRepairOrder(createdOrder.getOrderId()));
    }

    private static class SilentPrinter extends Printer {
        private RepairOrder printedRepairOrder;

        @Override
        public void printRepairOrder(RepairOrder repairOrder) {
            printedRepairOrder = repairOrder;
        }

        private RepairOrder getPrintedRepairOrder() {
            return printedRepairOrder;
        }
    }
}
