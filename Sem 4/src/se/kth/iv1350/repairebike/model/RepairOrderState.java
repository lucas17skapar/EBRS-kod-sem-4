package se.kth.iv1350.repairebike.model;

/**
 * States for a repair order.
 */
public enum RepairOrderState {
    NEWLY_CREATED,
    READY_FOR_APPROVAL,
    ACCEPTED,
    REJECTED
}
