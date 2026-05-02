package se.kth.iv1350.repairebike.integration;

import se.kth.iv1350.repairebike.model.RepairOrder;

/**
 * Prints repair orders.
 */
public class Printer {
    /**
     * Prints a complete repair order to standard output.
     *
     * @param repairOrder The repair order to print.
     */
    public void printRepairOrder(RepairOrder repairOrder) {
        if (repairOrder == null) {
            System.out.println("No repair order available to print.");
            return;
        }
        System.out.println(repairOrder.createPrintableStringRepresentation());
    }
}
