package se.kth.iv1350.repairebike.model;

/**
 * Gives a discount to bikes that are still covered by warranty.
 */
public class WarrantyDiscountStrategy implements DiscountStrategy {
    private static final String WARRANTY_SERIAL_NUMBER_PREFIX = "SN-123";
    private static final double WARRANTY_DISCOUNT_RATE = 0.10;

    /**
     * Calculates a warranty discount when the bike serial number indicates warranty coverage.
     *
     * @param repairOrder The repair order that might receive a discount.
     * @param totalCost The total cost before discount.
     * @return The warranty discount, or zero if no warranty applies.
     */
    @Override
    public Amount calculateDiscount(RepairOrder repairOrder, Amount totalCost) {
        String serialNumber = repairOrder.getBike().getSerialNumber();
        if (serialNumber.startsWith(WARRANTY_SERIAL_NUMBER_PREFIX)) {
            return totalCost.multiply(WARRANTY_DISCOUNT_RATE);
        }
        return new Amount(0.0);
    }
}
