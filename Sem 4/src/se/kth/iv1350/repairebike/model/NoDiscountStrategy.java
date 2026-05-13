package se.kth.iv1350.repairebike.model;

/**
 * A discount strategy that never applies a discount.
 */
public class NoDiscountStrategy implements DiscountStrategy {
    /**
     * Calculates no discount.
     *
     * @param repairOrder The repair order that might receive a discount.
     * @param totalCost The total cost before discount.
     * @return Zero discount.
     */
    @Override
    public Amount calculateDiscount(RepairOrder repairOrder, Amount totalCost) {
        return new Amount(0.0);
    }
}
