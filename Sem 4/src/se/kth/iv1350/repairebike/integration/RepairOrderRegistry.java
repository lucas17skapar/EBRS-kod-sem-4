package se.kth.iv1350.repairebike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.model.RepairOrder;

/**
 * Stores repair orders in memory.
 */
public class RepairOrderRegistry {
    private final List<RepairOrder> repairOrders = new ArrayList<>();
    private int nextRepairOrderId = 1;

    /**
     * Generates the next repair order id.
     *
     * @return The next repair order id.
     */
    public int generateRepairOrderId() {
        return nextRepairOrderId++;
    }

    /**
     * Adds a repair order to the registry.
     *
     * @param repairOrder The repair order to add.
     */
    public void addRepairOrder(RepairOrder repairOrder) {
        repairOrders.add(repairOrder);
    }

    /**
     * Finds a repair order by id.
     *
     * @param repairOrderId The repair order id.
     * @return The matching repair order, or {@code null} if no match is found.
     */
    public RepairOrder findRepairOrder(int repairOrderId) {
        for (RepairOrder repairOrder : repairOrders) {
            if (repairOrder.getOrderId() == repairOrderId) {
                return repairOrder;
            }
        }
        return null;
    }

}
