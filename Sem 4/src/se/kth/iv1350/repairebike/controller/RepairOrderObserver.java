package se.kth.iv1350.repairebike.controller;

import se.kth.iv1350.repairebike.dto.RepairOrderDTO;

/**
 * Receives notifications when a repair order has been updated.
 */
public interface RepairOrderObserver {
    /**
     * Called when a repair order has been updated.
     *
     * @param repairOrder The updated repair order.
     */
    void repairOrderUpdated(RepairOrderDTO repairOrder);
}
