package se.kth.iv1350.repairebike.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;

class RepairOrderTest {
    private RepairOrder repairOrder;

    @BeforeEach
    void setUp() {
        Bike bike = new Bike("Crescent", "Elina", "SN-12345");
        Customer customer = new Customer("Sara Lind", "0701234567", "sara.lind@example.com", bike);
        repairOrder = new RepairOrder(1, customer, bike, "Battery drains quickly.");
    }

    @Test
    void newlyCreatedRepairOrderHasNewlyCreatedState() {
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void repairOrderStoresOrderId() {
        int result = repairOrder.getOrderId();

        assertEquals(1, result);
    }

    @Test
    void addDiagnosticReportAndProposedRepairTasksStoresDiagnosticReport() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Loose connector detected.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        DiagnosticReport result = repairOrder.getDiagnosticReport();

        assertSame(diagnosticReport, result);
    }

    @Test
    void addDiagnosticReportAndProposedRepairTasksStoresRepairTasks() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Multiple issues detected.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        tasks.add(new RepairTask("Update firmware", new Amount(650.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        List<RepairTask> storedTasks = repairOrder.getRepairTasks();

        assertEquals(2, storedTasks.size());
        assertEquals("Replace connector", storedTasks.get(0).getDescription());
        assertEquals(new Amount(900.0), storedTasks.get(0).getCost());
        assertEquals("Update firmware", storedTasks.get(1).getDescription());
        assertEquals(new Amount(650.0), storedTasks.get(1).getCost());
    }

    @Test
    void addDiagnosticReportAndProposedRepairTasksDoesNotMakeOrderReadyForApproval() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Battery and firmware issues.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void prepareRepairOrderForApprovalChangesStateToReadyForApprovalWhenDiagnosticExists() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Battery and firmware issues.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.READY_FOR_APPROVAL, result);
    }

    @Test
    void prepareRepairOrderForApprovalDoesNotChangeStateWithoutDiagnosticReport() {
        repairOrder.prepareRepairOrderForApproval();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void prepareRepairOrderForApprovalDoesNotChangeStateWithoutRepairTasks() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("No repair tasks proposed.");
        List<RepairTask> tasks = new ArrayList<>();

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void addDiagnosticReportAndProposedRepairTasksSetsEstimatedCompletionDate() {
        LocalDate expectedCompletionDate = repairOrder.getCreatedDate().plusDays(3);
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        LocalDate result = repairOrder.getEstimatedCompletionDate();

        assertEquals(expectedCompletionDate, result);
    }

    @Test
    void addDiagnosticReportAndProposedRepairTasksStoresSpecifiedEstimatedCompletionDate() {
        LocalDate expectedCompletionDate = repairOrder.getCreatedDate().plusDays(5);
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks, expectedCompletionDate);
        LocalDate result = repairOrder.getEstimatedCompletionDate();

        assertEquals(expectedCompletionDate, result);
    }

    @Test
    void calculateTotalCostReturnsSumOfRepairTaskCosts() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector and firmware issues.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.50)));
        tasks.add(new RepairTask("Update firmware", new Amount(649.25)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        Amount totalCost = repairOrder.calculateTotalCost();

        assertEquals(new Amount(1549.75), totalCost);
    }

    @Test
    void calculateTotalCostWithNoDiscountStrategyReturnsSumOfRepairTaskCosts() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector and firmware issues.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        tasks.add(new RepairTask("Update firmware", new Amount(650.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        Amount totalCost = repairOrder.calculateTotalCost(new NoDiscountStrategy());

        assertEquals(new Amount(1550.0), totalCost);
    }

    @Test
    void calculateTotalCostWithWarrantyDiscountStrategySubtractsWarrantyDiscount() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector and firmware issues.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        tasks.add(new RepairTask("Update firmware", new Amount(650.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        Amount totalCost = repairOrder.calculateTotalCost(new WarrantyDiscountStrategy());

        assertEquals(new Amount(1395.0), totalCost);
    }

    @Test
    void acceptRepairOrderDoesNotChangeStateBeforeOrderIsReadyForApproval() {
        repairOrder.acceptRepairOrder();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void acceptRepairOrderChangesStateToAcceptedWhenOrderIsReadyForApproval() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();

        repairOrder.acceptRepairOrder();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.ACCEPTED, result);
    }

    @Test
    void acceptedRepairOrderCannotBeMadeReadyForApprovalAgain() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();
        repairOrder.acceptRepairOrder();

        repairOrder.prepareRepairOrderForApproval();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.ACCEPTED, result);
    }

    @Test
    void acceptedRepairOrderCannotReceiveNewDiagnosticReportAndRepairTasks() {
        DiagnosticReport originalReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> originalTasks = new ArrayList<>();
        RepairTask originalTask = new RepairTask("Replace connector", new Amount(900.0));
        originalTasks.add(originalTask);
        repairOrder.addDiagnosticReportAndProposedRepairTasks(originalReport, originalTasks);
        repairOrder.prepareRepairOrderForApproval();
        repairOrder.acceptRepairOrder();
        DiagnosticReport newReport = new DiagnosticReport("Motor issue.");
        List<RepairTask> newTasks = new ArrayList<>();
        newTasks.add(new RepairTask("Replace motor", new Amount(2000.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(newReport, newTasks);

        assertSame(originalReport, repairOrder.getDiagnosticReport());
        assertEquals(1, repairOrder.getRepairTasks().size());
        assertSame(originalTask, repairOrder.getRepairTasks().get(0));
        assertEquals(RepairOrderState.ACCEPTED, repairOrder.getState());
    }

    @Test
    void rejectRepairOrderChangesStateToRejectedWhenOrderIsReadyForApproval() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();

        repairOrder.rejectRepairOrder();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.REJECTED, result);
    }

    @Test
    void rejectRepairOrderDoesNotChangeStateBeforeOrderIsReadyForApproval() {
        repairOrder.rejectRepairOrder();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.NEWLY_CREATED, result);
    }

    @Test
    void rejectedRepairOrderCannotBeMadeReadyForApprovalAgain() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);
        repairOrder.prepareRepairOrderForApproval();
        repairOrder.rejectRepairOrder();

        repairOrder.prepareRepairOrderForApproval();
        RepairOrderState result = repairOrder.getState();

        assertEquals(RepairOrderState.REJECTED, result);
    }

    @Test
    void rejectedRepairOrderCannotReceiveNewDiagnosticReportAndRepairTasks() {
        DiagnosticReport originalReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> originalTasks = new ArrayList<>();
        RepairTask originalTask = new RepairTask("Replace connector", new Amount(900.0));
        originalTasks.add(originalTask);
        repairOrder.addDiagnosticReportAndProposedRepairTasks(originalReport, originalTasks);
        repairOrder.prepareRepairOrderForApproval();
        repairOrder.rejectRepairOrder();
        DiagnosticReport newReport = new DiagnosticReport("Motor issue.");
        List<RepairTask> newTasks = new ArrayList<>();
        newTasks.add(new RepairTask("Replace motor", new Amount(2000.0)));

        repairOrder.addDiagnosticReportAndProposedRepairTasks(newReport, newTasks);

        assertSame(originalReport, repairOrder.getDiagnosticReport());
        assertEquals(1, repairOrder.getRepairTasks().size());
        assertSame(originalTask, repairOrder.getRepairTasks().get(0));
        assertEquals(RepairOrderState.REJECTED, repairOrder.getState());
    }

    @Test
    void getRepairTasksReturnsDefensiveCopy() {
        DiagnosticReport diagnosticReport = new DiagnosticReport("Connector issue.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(diagnosticReport, tasks);

        List<RepairTask> firstRead = repairOrder.getRepairTasks();
        List<RepairTask> secondRead = repairOrder.getRepairTasks();
        firstRead.clear();

        assertNotSame(firstRead, secondRead);
        assertEquals(1, secondRead.size());
        assertEquals(1, repairOrder.getRepairTasks().size());
        assertNotNull(repairOrder.getRepairTasks().get(0));
    }
}
