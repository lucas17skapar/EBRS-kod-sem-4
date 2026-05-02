package se.kth.iv1350.repairebike.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a repair order.
 */
public class RepairOrder {
    private final int orderId;
    private final Customer customer;
    private final Bike bike;
    private final String problemDescription;
    private final LocalDate createdDate;
    private DiagnosticReport diagnosticReport;
    private final List<RepairTask> repairTasks;
    private RepairOrderState state;
    private LocalDate estimatedCompletionDate;

    /**
     * Creates a new repair order with an order id.
     *
     * @param orderId The repair order id.
     * @param customer The customer.
     * @param bike The bike.
     * @param problemDescription The problem description.
     */
    public RepairOrder(int orderId, Customer customer, Bike bike, String problemDescription) {
        this.orderId = orderId;
        this.customer = customer;
        this.bike = bike;
        this.problemDescription = problemDescription;
        this.createdDate = LocalDate.now();
        this.repairTasks = new ArrayList<>();
        this.state = RepairOrderState.NEWLY_CREATED;
    }

    /**
     * Gets the repair order id.
     *
     * @return The repair order id.
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Gets the customer.
     *
     * @return The customer.
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Gets the bike.
     *
     * @return The bike.
     */
    public Bike getBike() {
        return bike;
    }

    /**
     * Gets the problem description.
     *
     * @return The problem description.
     */
    public String getProblemDescription() {
        return problemDescription;
    }

    /**
     * Gets the created date.
     *
     * @return The created date.
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Gets the diagnostic report.
     *
     * @return The diagnostic report, or {@code null} if not set.
     */
    public DiagnosticReport getDiagnosticReport() {
        return diagnosticReport;
    }

    /**
     * Gets the repair tasks.
     *
     * @return A copy of the repair tasks.
     */
    public List<RepairTask> getRepairTasks() {
        return new ArrayList<>(repairTasks);
    }

    /**
     * Gets the state.
     *
     * @return The repair order state.
     */
    public RepairOrderState getState() {
        return state;
    }

    /**
     * Gets the estimated completion date.
     *
     * @return The estimated completion date, or {@code null} if not set.
     */
    public LocalDate getEstimatedCompletionDate() {
        return estimatedCompletionDate;
    }

    /**
     * Adds diagnostic information and proposed repair tasks.
     *
     * @param diagnosticReport The diagnostic report.
     * @param repairTasks The proposed repair tasks.
     */
    public void addDiagnosticReportAndProposedRepairTasks(
        DiagnosticReport diagnosticReport,
        List<RepairTask> repairTasks
    ) {
        addDiagnosticReportAndProposedRepairTasks(diagnosticReport, repairTasks, createdDate.plusDays(3));
    }

    /**
     * Adds diagnostic information, proposed repair tasks, and estimated completion date.
     *
     * @param diagnosticReport The diagnostic report.
     * @param repairTasks The proposed repair tasks.
     * @param estimatedCompletionDate The estimated completion date.
     */
    public void addDiagnosticReportAndProposedRepairTasks(
        DiagnosticReport diagnosticReport,
        List<RepairTask> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        if (isFinalState()) {
            return;
        }
        this.diagnosticReport = diagnosticReport;
        this.repairTasks.clear();
        this.repairTasks.addAll(repairTasks);
        this.estimatedCompletionDate = estimatedCompletionDate;
    }

    /**
     * Prepares this repair order for customer approval.
     */
    public void prepareRepairOrderForApproval() {
        if (diagnosticReport != null && !repairTasks.isEmpty() && !isFinalState()) {
            state = RepairOrderState.READY_FOR_APPROVAL;
        }
    }

    /**
     * Calculates the total cost of all proposed repair tasks.
     *
     * @return The total cost.
     */
    public Amount calculateTotalCost() {
        Amount totalCost = new Amount(0.0);
        for (RepairTask repairTask : repairTasks) {
            totalCost = totalCost.add(repairTask.getCost());
        }
        return totalCost;
    }

    /**
     * Accepts the repair order.
     */
    public void acceptRepairOrder() {
        if (state == RepairOrderState.READY_FOR_APPROVAL) {
            state = RepairOrderState.ACCEPTED;
        }
    }

    /**
     * Rejects the repair order.
     */
    public void rejectRepairOrder() {
        if (state == RepairOrderState.READY_FOR_APPROVAL) {
            state = RepairOrderState.REJECTED;
        }
    }

    private boolean isFinalState() {
        return state == RepairOrderState.ACCEPTED || state == RepairOrderState.REJECTED;
    }

    /**
     * Creates a printable string representation of this repair order.
     *
     * @return The printable string.
     */
    public String createPrintableStringRepresentation() {
        String lineSeparator = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        builder.append("Repair Order").append(lineSeparator);
        builder.append("Order ID: ").append(orderId).append(lineSeparator);
        builder.append("State: ").append(state).append(lineSeparator);
        builder.append("Created Date: ").append(createdDate).append(lineSeparator);
        builder.append("Estimated Completion Date: ").append(estimatedCompletionDate).append(lineSeparator);
        builder.append("Customer: ").append(customer).append(lineSeparator);
        builder.append("Bike: ").append(bike).append(lineSeparator);
        builder.append("Problem Description: ").append(problemDescription).append(lineSeparator);
        builder.append("Diagnostic Report: ").append(diagnosticReport).append(lineSeparator);
        builder.append("Repair Tasks: ").append(repairTasks).append(lineSeparator);
        builder.append("Total Cost: ").append(calculateTotalCost()).append(lineSeparator);
        return builder.toString();
    }

    /**
     * Returns a string representation of the repair order.
     *
     * @return A repair order string.
     */
    @Override
    public String toString() {
        return "RepairOrder{"
            + "orderId=" + orderId
            + ", customer=" + customer
            + ", bike=" + bike
            + ", problemDescription='" + problemDescription + "'"
            + ", createdDate=" + createdDate
            + ", diagnosticReport=" + diagnosticReport
            + ", repairTasks=" + repairTasks
            + ", state=" + state
            + ", estimatedCompletionDate=" + estimatedCompletionDate
            + "}";
    }
}
