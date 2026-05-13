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
     * @return {@code true} if the repair order was updated, otherwise {@code false}.
     */
    public boolean addDiagnosticReportAndProposedRepairTasks(
        DiagnosticReport diagnosticReport,
        List<RepairTask> repairTasks
    ) {
        return addDiagnosticReportAndProposedRepairTasks(diagnosticReport, repairTasks, createdDate.plusDays(3));
    }

    /**
     * Adds diagnostic information, proposed repair tasks, and estimated completion date.
     *
     * @param diagnosticReport The diagnostic report.
     * @param repairTasks The proposed repair tasks.
     * @param estimatedCompletionDate The estimated completion date.
     * @return {@code true} if the repair order was updated, otherwise {@code false}.
     */
    public boolean addDiagnosticReportAndProposedRepairTasks(
        DiagnosticReport diagnosticReport,
        List<RepairTask> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        if (isFinalState()) {
            return false;
        }
        this.diagnosticReport = diagnosticReport;
        this.repairTasks.clear();
        this.repairTasks.addAll(repairTasks);
        this.estimatedCompletionDate = estimatedCompletionDate;
        return true;
    }

    /**
     * Prepares this repair order for customer approval.
     *
     * @return {@code true} if the repair order state changed, otherwise {@code false}.
     */
    public boolean prepareRepairOrderForApproval() {
        if (diagnosticReport != null && !repairTasks.isEmpty() && !isFinalState()) {
            state = RepairOrderState.READY_FOR_APPROVAL;
            return true;
        }
        return false;
    }

    /**
     * Calculates the total cost of all proposed repair tasks.
     *
     * @return The total cost.
     */
    public Amount calculateTotalCost() {
        return calculateTotalCost(new NoDiscountStrategy());
    }

    /**
     * Calculates the total cost of all proposed repair tasks after discount.
     *
     * @param discountStrategy The strategy used to calculate the discount.
     * @return The total cost after discount.
     */
    public Amount calculateTotalCost(DiscountStrategy discountStrategy) {
        Amount totalCost = new Amount(0.0);
        for (RepairTask repairTask : repairTasks) {
            totalCost = totalCost.add(repairTask.getCost());
        }
        Amount discount = discountStrategy.calculateDiscount(this, totalCost);
        return totalCost.subtract(discount);
    }

    /**
     * Accepts the repair order.
     *
     * @return {@code true} if the repair order state changed, otherwise {@code false}.
     */
    public boolean acceptRepairOrder() {
        if (state == RepairOrderState.READY_FOR_APPROVAL) {
            state = RepairOrderState.ACCEPTED;
            return true;
        }
        return false;
    }

    /**
     * Rejects the repair order.
     *
     * @return {@code true} if the repair order state changed, otherwise {@code false}.
     */
    public boolean rejectRepairOrder() {
        if (state == RepairOrderState.READY_FOR_APPROVAL) {
            state = RepairOrderState.REJECTED;
            return true;
        }
        return false;
    }

    private boolean isFinalState() {
        return state == RepairOrderState.ACCEPTED || state == RepairOrderState.REJECTED;
    }

}
