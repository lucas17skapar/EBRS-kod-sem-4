package se.kth.iv1350.repairebike.view;

import se.kth.iv1350.repairebike.controller.RepairOrderObserver;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;

/**
 * Shows repair order updates to technicians and receptionists.
 */
public class RepairOrderView implements RepairOrderObserver {
    /**
     * Prints the updated repair order to standard output.
     *
     * @param repairOrder The updated repair order.
     */
    @Override
    public void repairOrderUpdated(RepairOrderDTO repairOrder) {
        System.out.println("RepairOrderView update: " + repairOrder);
    }
}
