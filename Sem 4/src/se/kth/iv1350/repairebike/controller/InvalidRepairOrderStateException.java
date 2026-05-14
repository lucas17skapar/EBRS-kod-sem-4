package se.kth.iv1350.repairebike.controller;

/**
 * Thrown when a repair order operation is not allowed in the current state.
 */
public class InvalidRepairOrderStateException extends Exception {
    private final int repairOrderId;

    /**
     * Creates a new exception for an invalid repair order state.
     *
     * @param repairOrderId The repair order id.
     * @param message A message explaining why the operation is not allowed.
     */
    public InvalidRepairOrderStateException(int repairOrderId, String message) {
        super("Repair order " + repairOrderId + ": " + message);
        this.repairOrderId = repairOrderId;
    }

    /**
     * Gets the repair order id.
     *
     * @return The repair order id.
     */
    public int getRepairOrderId() {
        return repairOrderId;
    }
}
