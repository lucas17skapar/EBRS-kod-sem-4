package se.kth.iv1350.repairebike.controller;

/**
 * Thrown when no repair order exists for a searched repair order id.
 */
public class RepairOrderNotFoundException extends Exception {
    private final int searchedRepairOrderId;

    /**
     * Creates a new exception for a missing repair order.
     *
     * @param searchedRepairOrderId The repair order id that did not match any repair order.
     */
    public RepairOrderNotFoundException(int searchedRepairOrderId) {
        super("No repair order found with id: " + searchedRepairOrderId);
        this.searchedRepairOrderId = searchedRepairOrderId;
    }

    /**
     * Gets the repair order id that did not match any repair order.
     *
     * @return The searched repair order id.
     */
    public int getSearchedRepairOrderId() {
        return searchedRepairOrderId;
    }
}
