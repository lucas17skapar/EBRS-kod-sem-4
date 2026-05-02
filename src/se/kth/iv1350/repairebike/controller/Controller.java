package se.kth.iv1350.repairebike.controller;

import java.time.LocalDate;
import java.util.List;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.DiagnosticReport;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairOrderState;
import se.kth.iv1350.repairebike.model.RepairTask;

/**
 * Coordinates calls between view, integration layer, and model.
 */
public class Controller {
    private final CustomerRegistry customerRegistry;
    private final RepairOrderRegistry repairOrderRegistry;
    private final Printer printer;
    private Customer currentCustomer;

    /**
     * Creates a controller with required dependencies.
     *
     * @param customerRegistry The customer registry.
     * @param repairOrderRegistry The repair order registry.
     * @param printer The printer.
     */
    public Controller(
        CustomerRegistry customerRegistry,
        RepairOrderRegistry repairOrderRegistry,
        Printer printer
    ) {
        this.customerRegistry = customerRegistry;
        this.repairOrderRegistry = repairOrderRegistry;
        this.printer = printer;
    }

    /**
     * Finds a customer by phone number.
     *
     * @param phoneNumber The customer's phone number.
     * @return The found customer, or {@code null} if not found.
     */
    public Customer findCustomer(String phoneNumber) {
        currentCustomer = customerRegistry.findCustomerByPhoneNumber(phoneNumber);
        return currentCustomer;
    }

    /**
     * Creates a new repair order for the current customer.
     *
     * @param problemDescription The customer's problem description.
     * @return The created repair order, or {@code null} if no customer is selected.
     */
    public RepairOrder createRepairOrder(String problemDescription) {
        if (currentCustomer == null) {
            return null;
        }

        int repairOrderId = repairOrderRegistry.generateRepairOrderId();
        RepairOrder repairOrder = new RepairOrder(
            repairOrderId,
            currentCustomer,
            currentCustomer.getBike(),
            problemDescription
        );
        repairOrderRegistry.addRepairOrder(repairOrder);
        return repairOrder;
    }

    /**
     * Finds a repair order by id.
     *
     * @param repairOrderId The repair order id.
     * @return The matching repair order, or {@code null} if no match is found.
     */
    public RepairOrder findRepairOrder(int repairOrderId) {
        return repairOrderRegistry.findRepairOrder(repairOrderId);
    }

    /**
     * Adds a diagnostic report and proposed repair tasks to a repair order.
     *
     * @param repairOrderId The repair order id.
     * @param reportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @return The updated repair order, or {@code null} if no matching order exists.
     */
    public RepairOrder addDiagnosticReport(int repairOrderId, String reportText, List<RepairTask> repairTasks) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        return addDiagnosticReportToRepairOrder(
            repairOrder,
            reportText,
            repairTasks,
            repairOrder.getCreatedDate().plusDays(3)
        );
    }

    /**
     * Adds a diagnostic report, proposed repair tasks, and estimated completion date to a repair order.
     *
     * @param repairOrderId The repair order id.
     * @param reportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @param estimatedCompletionDate The estimated completion date.
     * @return The updated repair order, or {@code null} if no matching order exists.
     */
    public RepairOrder addDiagnosticReport(
        int repairOrderId,
        String reportText,
        List<RepairTask> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        return addDiagnosticReportToRepairOrder(repairOrder, reportText, repairTasks, estimatedCompletionDate);
    }

    /**
     * Prepares a repair order for customer approval.
     *
     * @param repairOrderId The repair order id.
     * @return The prepared repair order, or {@code null} if no matching order exists.
     */
    public RepairOrder prepareRepairOrderForApproval(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        repairOrder.prepareRepairOrderForApproval();
        return repairOrder;
    }

    /**
     * Accepts a repair order and prints it if it was accepted.
     *
     * @param repairOrderId The repair order id.
     * @return The accepted repair order, or {@code null} if no matching order exists.
     */
    public RepairOrder acceptRepairOrder(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        RepairOrderState previousState = repairOrder.getState();
        repairOrder.acceptRepairOrder();
        boolean wasAccepted = previousState != RepairOrderState.ACCEPTED
            && repairOrder.getState() == RepairOrderState.ACCEPTED;
        if (wasAccepted) {
            printer.printRepairOrder(repairOrder);
        }
        return repairOrder;
    }

    /**
     * Rejects a repair order.
     *
     * @param repairOrderId The repair order id.
     * @return The rejected repair order, or {@code null} if no matching order exists.
     */
    public RepairOrder rejectRepairOrder(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        repairOrder.rejectRepairOrder();
        return repairOrder;
    }

    /**
     * Prints a repair order.
     *
     * @param repairOrderId The repair order id.
     */
    public void printRepairOrder(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder != null) {
            printer.printRepairOrder(repairOrder);
        }
    }

    private RepairOrder addDiagnosticReportToRepairOrder(
        RepairOrder repairOrder,
        String reportText,
        List<RepairTask> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        DiagnosticReport diagnosticReport = new DiagnosticReport(reportText);
        repairOrder.addDiagnosticReportAndProposedRepairTasks(
            diagnosticReport,
            repairTasks,
            estimatedCompletionDate
        );
        return repairOrder;
    }

}
