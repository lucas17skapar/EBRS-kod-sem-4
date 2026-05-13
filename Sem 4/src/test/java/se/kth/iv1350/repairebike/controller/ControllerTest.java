package se.kth.iv1350.repairebike.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.DatabaseFailureException;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ControllerTest {
    private Controller controller;
    private SilentPrinter printer;

    @BeforeEach
    void setUp() {
        CustomerRegistry customerRegistry = CustomerRegistry.getInstance();
        RepairOrderRegistry repairOrderRegistry = new RepairOrderRegistry();
        printer = new SilentPrinter();
        controller = new Controller(customerRegistry, repairOrderRegistry, printer);
    }

    @Test
    void findCustomerReturnsCorrectCustomerForKnownPhoneNumber() throws Exception {
        CustomerDTO result = controller.findCustomer("0701234567");

        assertNotNull(result);
        assertEquals("Sara Lind", result.getName());
        assertEquals("0701234567", result.getPhoneNumber());
    }

    @Test
    void findCustomerThrowsExceptionForUnknownPhoneNumber() {
        CustomerNotFoundException exception = assertThrows(
            CustomerNotFoundException.class,
            () -> controller.findCustomer("0000000000")
        );

        assertEquals("0000000000", exception.getSearchedPhoneNumber());
    }

    @Test
    void findCustomerThrowsOperationFailedExceptionWhenDatabaseFails() {
        OperationFailedException exception = assertThrows(
            OperationFailedException.class,
            () -> controller.findCustomer(CustomerRegistry.DATABASE_FAILURE_PHONE_NUMBER)
        );

        assertTrue(exception.getCause() instanceof DatabaseFailureException);
    }

    @Test
    void missingCustomerSearchDoesNotChangeSelectedCustomer() throws Exception {
        controller.findCustomer("0701234567");

        assertThrows(CustomerNotFoundException.class, () -> controller.findCustomer("0000000000"));
        RepairOrderDTO createdRepairOrder = controller.createRepairOrder("Battery drains quickly.");

        assertNotNull(createdRepairOrder);
        assertEquals("Sara Lind", createdRepairOrder.getCustomer().getName());
    }

    @Test
    void failedCustomerSearchDoesNotChangeSelectedCustomer() throws Exception {
        controller.findCustomer("0701234567");

        assertThrows(
            OperationFailedException.class,
            () -> controller.findCustomer(CustomerRegistry.DATABASE_FAILURE_PHONE_NUMBER)
        );
        RepairOrderDTO createdRepairOrder = controller.createRepairOrder("Battery drains quickly.");

        assertNotNull(createdRepairOrder);
        assertEquals("Sara Lind", createdRepairOrder.getCustomer().getName());
    }

    @Test
    void createRepairOrderReturnsNullWhenNoCustomerHasBeenSelected() {
        RepairOrderDTO result = controller.createRepairOrder("Battery drains quickly.");

        assertNull(result);
    }

    @Test
    void createRepairOrderCreatesAndRegistersRepairOrderWhenCustomerExists() throws Exception {
        controller.findCustomer("0701234567");

        RepairOrderDTO createdRepairOrder = controller.createRepairOrder("Battery drains quickly.");

        assertNotNull(createdRepairOrder);
        RepairOrderDTO registeredRepairOrder = controller.findRepairOrder(createdRepairOrder.getOrderId());
        assertNotNull(registeredRepairOrder);
        assertEquals(1, registeredRepairOrder.getOrderId());
        assertEquals("Battery drains quickly.", registeredRepairOrder.getProblemDescription());
    }

    @Test
    void createRepairOrderCreatesUniqueRepairOrderIds() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO firstOrder = controller.createRepairOrder("First issue.");
        RepairOrderDTO secondOrder = controller.createRepairOrder("Second issue.");

        assertNotNull(firstOrder);
        assertNotNull(secondOrder);
        RepairOrderDTO firstResult = controller.findRepairOrder(firstOrder.getOrderId());
        RepairOrderDTO secondResult = controller.findRepairOrder(secondOrder.getOrderId());
        assertEquals(firstOrder.getOrderId(), firstResult.getOrderId());
        assertEquals(secondOrder.getOrderId(), secondResult.getOrderId());
        assertEquals(1, firstOrder.getOrderId());
        assertEquals(2, secondOrder.getOrderId());
    }

    @Test
    void findRepairOrderReturnsOrderWithMatchingId() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO firstOrder = controller.createRepairOrder("First issue.");
        RepairOrderDTO secondOrder = controller.createRepairOrder("Second issue.");

        RepairOrderDTO result = controller.findRepairOrder(firstOrder.getOrderId());

        assertNotNull(secondOrder);
        assertEquals(firstOrder.getOrderId(), result.getOrderId());
    }

    @Test
    void addDiagnosticReportReturnsNullWhenNoRepairOrderMatchesId() {
        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));

        RepairOrderDTO result = controller.addDiagnosticReport(999, "Connector issue.", tasks);

        assertNull(result);
    }

    @Test
    void addDiagnosticReportUpdatesSpecifiedRepairOrderCorrectly() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        tasks.add(new RepairTaskDTO("Update firmware", 650.0));

        RepairOrderDTO updatedOrder = controller.addDiagnosticReport(
            createdOrder.getOrderId(),
            "Connector and firmware issues.",
            tasks
        );

        assertNotNull(updatedOrder);
        assertEquals("Connector and firmware issues.", updatedOrder.getDiagnosticReportText());
        assertEquals(2, updatedOrder.getRepairTasks().size());
        assertEquals(1550.0, updatedOrder.getTotalCost(), 0.001);
        assertEquals("NEWLY_CREATED", updatedOrder.getState());
        assertEquals(updatedOrder.getOrderId(), controller.findRepairOrder(createdOrder.getOrderId()).getOrderId());
    }

    @Test
    void addDiagnosticReportUpdatesSpecifiedRepairOrderInsteadOfLatestOrder() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO firstOrder = controller.createRepairOrder("First issue.");
        RepairOrderDTO secondOrder = controller.createRepairOrder("Second issue.");
        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));

        RepairOrderDTO updatedOrder = controller.addDiagnosticReport(
            firstOrder.getOrderId(),
            "Connector issue.",
            tasks
        );

        assertEquals(firstOrder.getOrderId(), updatedOrder.getOrderId());
        assertEquals("Connector issue.", updatedOrder.getDiagnosticReportText());
        assertNull(controller.findRepairOrder(secondOrder.getOrderId()).getDiagnosticReportText());
        assertEquals(secondOrder.getOrderId(), controller.findRepairOrder(secondOrder.getOrderId()).getOrderId());
    }

    @Test
    void addDiagnosticReportStoresSpecifiedEstimatedCompletionDate() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        LocalDate expectedCompletionDate = createdOrder.getCreatedDate().plusDays(5);

        RepairOrderDTO updatedOrder = controller.addDiagnosticReport(
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
        RepairOrderDTO result = controller.prepareRepairOrderForApproval(999);

        assertNull(result);
    }

    @Test
    void prepareRepairOrderForApprovalChangesSpecifiedOrderStateWhenDiagnosticExists() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");

        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);

        RepairOrderDTO preparedOrder = controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        assertNotNull(preparedOrder);
        assertEquals("READY_FOR_APPROVAL", preparedOrder.getState());
        assertEquals(preparedOrder.getOrderId(), controller.findRepairOrder(createdOrder.getOrderId()).getOrderId());
    }

    @Test
    void acceptRepairOrderReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrderDTO result = controller.acceptRepairOrder(999);

        assertNull(result);
    }

    @Test
    void acceptRepairOrderChangesStateToAcceptedWhenSpecifiedRepairOrderIsReadyForApproval() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");
        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);
        controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        RepairOrderDTO acceptedOrder = controller.acceptRepairOrder(createdOrder.getOrderId());

        assertNotNull(acceptedOrder);
        assertEquals("ACCEPTED", acceptedOrder.getState());
        assertEquals(acceptedOrder.getOrderId(), controller.findRepairOrder(createdOrder.getOrderId()).getOrderId());
    }

    @Test
    void acceptRepairOrderDoesNotAcceptOrderBeforeItIsReadyForApproval() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");

        RepairOrderDTO acceptedOrder = controller.acceptRepairOrder(createdOrder.getOrderId());

        assertNotNull(acceptedOrder);
        assertEquals("NEWLY_CREATED", acceptedOrder.getState());
    }

    @Test
    void rejectRepairOrderReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrderDTO result = controller.rejectRepairOrder(999);

        assertNull(result);
    }

    @Test
    void rejectRepairOrderChangesStateToRejectedWhenSpecifiedRepairOrderIsReadyForApproval() throws Exception {
        controller.findCustomer("0701234567");
        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");
        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);
        controller.prepareRepairOrderForApproval(createdOrder.getOrderId());

        RepairOrderDTO rejectedOrder = controller.rejectRepairOrder(createdOrder.getOrderId());

        assertNotNull(rejectedOrder);
        assertEquals("REJECTED", rejectedOrder.getState());
        assertEquals(rejectedOrder.getOrderId(), controller.findRepairOrder(createdOrder.getOrderId()).getOrderId());
    }

    @Test
    void printRepairOrderPrintsSpecifiedText() {
        controller.printRepairOrder("Repair Order");

        assertEquals("Repair Order", printer.getPrintedRepairOrder());
    }

    @Test
    void observerIsNotifiedWhenRepairOrderIsUpdated() throws Exception {
        RecordingRepairOrderObserver observer = new RecordingRepairOrderObserver();
        controller.addRepairOrderObserver(observer);
        controller.findCustomer("0701234567");

        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly.");
        List<RepairTaskDTO> tasks = new ArrayList<>();
        tasks.add(new RepairTaskDTO("Replace connector", 900.0));
        controller.addDiagnosticReport(createdOrder.getOrderId(), "Connector issue.", tasks);

        assertEquals(2, observer.getUpdatedRepairOrders().size());
        assertEquals(createdOrder.getOrderId(), observer.getUpdatedRepairOrders().get(0).getOrderId());
        assertEquals("Connector issue.", observer.getUpdatedRepairOrders().get(1).getDiagnosticReportText());
    }

    private static class SilentPrinter extends Printer {
        private String printedRepairOrder;

        @Override
        public void printRepairOrder(String printableRepairOrder) {
            printedRepairOrder = printableRepairOrder;
        }

        private String getPrintedRepairOrder() {
            return printedRepairOrder;
        }
    }

    private static class RecordingRepairOrderObserver implements RepairOrderObserver {
        private final List<RepairOrderDTO> updatedRepairOrders = new ArrayList<>();

        @Override
        public void repairOrderUpdated(RepairOrderDTO repairOrder) {
            updatedRepairOrders.add(repairOrder);
        }

        private List<RepairOrderDTO> getUpdatedRepairOrders() {
            return updatedRepairOrders;
        }
    }
}
