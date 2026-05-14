package se.kth.iv1350.repairebike.model;

/**
 * Receives notifications when a repair order has been updated.
 */
public interface RepairOrderObserver {
    /**
     * Called when a repair order has been updated.
     *
     * @param repairOrder The updated repair order snapshot.
     */
    void repairOrderUpdated(RepairOrderSnapshot repairOrder);
}
