package se.kth.iv1350.repairebike.model;

import java.time.LocalDate;

/**
 * Gives a discount to bikes that are still covered by warranty.
 */
public class WarrantyDiscountStrategy implements DiscountStrategy {
    private static final double WARRANTY_DISCOUNT_RATE = 0.10;

    /**
     * Calculates a warranty discount when the bike is covered by warranty.
     *
     * @param repairOrder The repair order that might receive a discount.
     * @param totalCost The total cost before discount.
     * @return The warranty discount, or zero if no warranty applies.
     */
    @Override
    public Amount calculateDiscount(RepairOrder repairOrder, Amount totalCost) {
        if (repairOrder.getBike().isUnderWarranty(LocalDate.now())) {
            return totalCost.multiply(WARRANTY_DISCOUNT_RATE);
        }
        return new Amount(0.0);
    }
}
