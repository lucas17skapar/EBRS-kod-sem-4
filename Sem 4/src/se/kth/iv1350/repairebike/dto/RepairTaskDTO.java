package se.kth.iv1350.repairebike.dto;

/**
 * Contains a proposed repair task that may be sent between the view and controller.
 */
public class RepairTaskDTO {
    private final String description;
    private final double cost;

    /**
     * Creates a new repair task DTO.
     *
     * @param description The repair task description.
     * @param cost The estimated repair task cost.
     */
    public RepairTaskDTO(String description, double cost) {
        this.description = description;
        this.cost = cost;
    }

    /**
     * Gets the repair task description.
     *
     * @return The repair task description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the estimated repair task cost.
     *
     * @return The estimated repair task cost.
     */
    public double getCost() {
        return cost;
    }
}
