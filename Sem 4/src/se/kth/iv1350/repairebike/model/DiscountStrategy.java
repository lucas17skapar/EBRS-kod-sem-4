package se.kth.iv1350.repairebike.model;

/**
 * Defines how a discount is calculated for a repair order.
 */
public interface DiscountStrategy {
    /**
     * Calculates the discount for the specified repair order.
     *
     * @param repairOrder The repair order that might receive a discount.
     * @param totalCost The total cost before discount.
     * @return The discount amount.
     */
    Amount calculateDiscount(RepairOrder repairOrder, Amount totalCost);
}
