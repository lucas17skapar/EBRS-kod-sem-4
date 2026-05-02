package se.kth.iv1350.repairebike.model;

/**
 * Represents a proposed repair task.
 */
public class RepairTask {
    private final String description;
    private final Amount cost;

    /**
     * Creates a repair task.
     *
     * @param description The task description.
     * @param cost The estimated task cost.
     */
    public RepairTask(String description, Amount cost) {
        this.description = description;
        this.cost = cost;
    }

    /**
     * Gets the task description.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the task cost.
     *
     * @return The cost.
     */
    public Amount getCost() {
        return cost;
    }

    /**
     * Returns a string representation of the repair task.
     *
     * @return A repair task string.
     */
    @Override
    public String toString() {
        return "RepairTask{description='" + description + "', cost=" + cost + "}";
    }
}
