package se.kth.iv1350.repairebike.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Immutable repair order data sent to observers.
 */
public class RepairOrderSnapshot {
    private final int orderId;
    private final Customer customer;
    private final Bike bike;
    private final String problemDescription;
    private final LocalDate createdDate;
    private final String diagnosticReportText;
    private final List<RepairTask> repairTasks;
    private final RepairOrderState state;
    private final LocalDate estimatedCompletionDate;
    private final Amount totalCost;

    /**
     * Creates a repair order snapshot.
     *
     * @param repairOrder The repair order to copy data from.
     * @param totalCost The current total cost.
     */
    public RepairOrderSnapshot(RepairOrder repairOrder, Amount totalCost) {
        this.orderId = repairOrder.getOrderId();
        this.customer = repairOrder.getCustomer();
        this.bike = repairOrder.getBike();
        this.problemDescription = repairOrder.getProblemDescription();
        this.createdDate = repairOrder.getCreatedDate();
        this.diagnosticReportText = getDiagnosticReportText(repairOrder);
        this.repairTasks = repairOrder.getRepairTasks();
        this.state = repairOrder.getState();
        this.estimatedCompletionDate = repairOrder.getEstimatedCompletionDate();
        this.totalCost = totalCost;
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
     * Gets the date when the repair order was created.
     *
     * @return The date when the repair order was created.
     */
    public LocalDate getCreatedDate() {
        return createdDate;
    }

    /**
     * Gets the diagnostic report text.
     *
     * @return The diagnostic report text, or {@code null} if no report exists.
     */
    public String getDiagnosticReportText() {
        return diagnosticReportText;
    }

    /**
     * Gets the proposed repair tasks.
     *
     * @return A copy of the proposed repair tasks.
     */
    public List<RepairTask> getRepairTasks() {
        return new ArrayList<>(repairTasks);
    }

    /**
     * Gets the repair order state.
     *
     * @return The repair order state.
     */
    public RepairOrderState getState() {
        return state;
    }

    /**
     * Gets the estimated completion date.
     *
     * @return The estimated completion date, or {@code null} if no date exists.
     */
    public LocalDate getEstimatedCompletionDate() {
        return estimatedCompletionDate;
    }

    /**
     * Gets the total estimated repair cost.
     *
     * @return The total estimated repair cost.
     */
    public Amount getTotalCost() {
        return totalCost;
    }

    private String getDiagnosticReportText(RepairOrder repairOrder) {
        if (repairOrder.getDiagnosticReport() == null) {
            return null;
        }
        return repairOrder.getDiagnosticReport().getReportText();
    }
}
