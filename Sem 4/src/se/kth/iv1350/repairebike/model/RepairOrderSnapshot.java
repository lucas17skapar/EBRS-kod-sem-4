package se.kth.iv1350.repairebike.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Immutable repair order data sent to observers.
 */
public class RepairOrderSnapshot {
    private final int orderId;
    private final CustomerData customer;
    private final BikeData bike;
    private final String problemDescription;
    private final LocalDate createdDate;
    private final String diagnosticReportText;
    private final List<RepairTaskData> repairTasks;
    private final RepairOrderState state;
    private final LocalDate estimatedCompletionDate;
    private final BigDecimal totalCost;

    /**
     * Immutable customer data sent to observers.
     *
     * @param name The customer name.
     * @param phoneNumber The customer phone number.
     * @param email The customer email address.
     */
    public record CustomerData(String name, String phoneNumber, String email) {
    }

    /**
     * Immutable bike data sent to observers.
     *
     * @param brand The bike brand.
     * @param model The bike model.
     * @param serialNumber The bike serial number.
     * @param warrantyEndDate The last day covered by warranty.
     */
    public record BikeData(String brand, String model, String serialNumber, LocalDate warrantyEndDate) {
    }

    /**
     * Immutable repair task data sent to observers.
     *
     * @param description The task description.
     * @param cost The estimated task cost.
     */
    public record RepairTaskData(String description, BigDecimal cost) {
    }

    /**
     * Creates a repair order snapshot.
     *
     * @param repairOrder The repair order to copy data from.
     * @param totalCost The current total cost.
     */
    public RepairOrderSnapshot(RepairOrder repairOrder, Amount totalCost) {
        this.orderId = repairOrder.getOrderId();
        this.customer = createCustomerData(repairOrder.getCustomer());
        this.bike = createBikeData(repairOrder.getBike());
        this.problemDescription = repairOrder.getProblemDescription();
        this.createdDate = repairOrder.getCreatedDate();
        this.diagnosticReportText = getDiagnosticReportText(repairOrder);
        this.repairTasks = repairOrder.getRepairTasks().stream()
            .map(this::createRepairTaskData)
            .toList();
        this.state = repairOrder.getState();
        this.estimatedCompletionDate = repairOrder.getEstimatedCompletionDate();
        this.totalCost = totalCost.asBigDecimal();
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
     * Gets copied customer data.
     *
     * @return The copied customer data.
     */
    public CustomerData getCustomer() {
        return customer;
    }

    /**
     * Gets copied bike data.
     *
     * @return The copied bike data.
     */
    public BikeData getBike() {
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
     * Gets copied proposed repair tasks.
     *
     * @return The copied proposed repair tasks.
     */
    public List<RepairTaskData> getRepairTasks() {
        return repairTasks;
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
    public BigDecimal getTotalCost() {
        return totalCost;
    }

    private CustomerData createCustomerData(Customer customer) {
        return new CustomerData(customer.getName(), customer.getPhoneNumber(), customer.getEmail());
    }

    private BikeData createBikeData(Bike bike) {
        return new BikeData(bike.getBrand(), bike.getModel(), bike.getSerialNumber(), bike.getWarrantyEndDate());
    }

    private RepairTaskData createRepairTaskData(RepairTask repairTask) {
        return new RepairTaskData(repairTask.getDescription(), repairTask.getCost().asBigDecimal());
    }

    private String getDiagnosticReportText(RepairOrder repairOrder) {
        if (repairOrder.getDiagnosticReport() == null) {
            return null;
        }
        return repairOrder.getDiagnosticReport().getReportText();
    }
}
