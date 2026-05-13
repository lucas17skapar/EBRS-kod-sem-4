package se.kth.iv1350.repairebike.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains repair order data that may be shown by the view.
 */
public class RepairOrderDTO {
    private final int orderId;
    private final CustomerDTO customer;
    private final BikeDTO bike;
    private final String problemDescription;
    private final LocalDate createdDate;
    private final String diagnosticReportText;
    private final List<RepairTaskDTO> repairTasks;
    private final String state;
    private final LocalDate estimatedCompletionDate;
    private final double totalCost;

    /**
     * Creates a new repair order DTO.
     *
     * @param orderId The repair order id.
     * @param customer The customer.
     * @param bike The bike.
     * @param problemDescription The problem description.
     * @param createdDate The date when the repair order was created.
     * @param diagnosticReportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @param state The repair order state.
     * @param estimatedCompletionDate The estimated completion date.
     * @param totalCost The total estimated repair cost.
     */
    public RepairOrderDTO(
        int orderId,
        CustomerDTO customer,
        BikeDTO bike,
        String problemDescription,
        LocalDate createdDate,
        String diagnosticReportText,
        List<RepairTaskDTO> repairTasks,
        String state,
        LocalDate estimatedCompletionDate,
        double totalCost
    ) {
        this.orderId = orderId;
        this.customer = customer;
        this.bike = bike;
        this.problemDescription = problemDescription;
        this.createdDate = createdDate;
        this.diagnosticReportText = diagnosticReportText;
        this.repairTasks = new ArrayList<>(repairTasks);
        this.state = state;
        this.estimatedCompletionDate = estimatedCompletionDate;
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
    public CustomerDTO getCustomer() {
        return customer;
    }

    /**
     * Gets the bike.
     *
     * @return The bike.
     */
    public BikeDTO getBike() {
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
    public List<RepairTaskDTO> getRepairTasks() {
        return new ArrayList<>(repairTasks);
    }

    /**
     * Gets the repair order state.
     *
     * @return The repair order state.
     */
    public String getState() {
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
    public double getTotalCost() {
        return totalCost;
    }

    /**
     * Gets a string representation of this repair order.
     *
     * @return A string representation of this repair order.
     */
    @Override
    public String toString() {
        return "RepairOrder{"
            + "orderId=" + orderId
            + ", customer=" + formatCustomer(customer)
            + ", bike=" + formatBike(bike)
            + ", problemDescription='" + problemDescription + "'"
            + ", createdDate=" + createdDate
            + ", diagnosticReportText='" + formatNullable(diagnosticReportText) + "'"
            + ", repairTasks=" + formatRepairTasks(repairTasks)
            + ", state=" + state
            + ", estimatedCompletionDate=" + estimatedCompletionDate
            + ", totalCost=" + formatAmount(totalCost)
            + "}";
    }

    private static String formatCustomer(CustomerDTO customer) {
        if (customer == null) {
            return "none";
        }

        return "Customer{"
            + "name='" + customer.getName() + "'"
            + ", phoneNumber='" + customer.getPhoneNumber() + "'"
            + ", email='" + customer.getEmail() + "'"
            + ", bike=" + formatBike(customer.getBike())
            + "}";
    }

    private static String formatBike(BikeDTO bike) {
        if (bike == null) {
            return "none";
        }

        return "Bike{"
            + "brand='" + bike.getBrand() + "'"
            + ", model='" + bike.getModel() + "'"
            + ", serialNumber='" + bike.getSerialNumber() + "'"
            + "}";
    }

    private static String formatRepairTasks(List<RepairTaskDTO> repairTasks) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < repairTasks.size(); i++) {
            RepairTaskDTO repairTask = repairTasks.get(i);
            builder.append("RepairTask{")
                .append("description='").append(repairTask.getDescription()).append("'")
                .append(", cost=").append(formatAmount(repairTask.getCost()))
                .append("}");
            if (i < repairTasks.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private static String formatAmount(double amount) {
        if (amount == Math.rint(amount)) {
            return Long.toString(Math.round(amount));
        }
        return Double.toString(amount);
    }

    private static String formatNullable(String text) {
        if (text == null) {
            return "none";
        }
        return text;
    }
}
