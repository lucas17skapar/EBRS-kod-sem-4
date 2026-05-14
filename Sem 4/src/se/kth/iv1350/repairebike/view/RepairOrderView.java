package se.kth.iv1350.repairebike.view;

import se.kth.iv1350.repairebike.model.RepairOrderObserver;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot;

/**
 * Shows repair order updates to technicians and receptionists.
 */
public class RepairOrderView implements RepairOrderObserver {
    /**
     * Prints the updated repair order to standard output.
     *
     * @param repairOrder The updated repair order snapshot.
     */
    @Override
    public void repairOrderUpdated(RepairOrderSnapshot repairOrder) {
        System.out.println("RepairOrderView update: " + RepairOrderFormatter.formatRepairOrder(repairOrder));
    }
}
