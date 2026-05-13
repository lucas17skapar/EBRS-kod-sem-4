package se.kth.iv1350.repairebike.integration;

/**
 * Prints repair orders.
 */
public class Printer {
    /**
     * Prints a formatted repair order to standard output.
     *
     * @param printableRepairOrder The formatted repair order to print.
     */
    public void printRepairOrder(String printableRepairOrder) {
        if (printableRepairOrder == null) {
            System.out.println("No repair order available to print.");
            return;
        }
        System.out.println(printableRepairOrder);
    }
}
