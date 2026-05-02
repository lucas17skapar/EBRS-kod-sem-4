package se.kth.iv1350.repairebike.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AmountTest {
    @Test
    void addReturnsSumOfTwoAmounts() {
        Amount firstAmount = new Amount(900.50);
        Amount secondAmount = new Amount(649.25);

        Amount result = firstAmount.add(secondAmount);

        assertEquals(new Amount(1549.75), result);
    }

    @Test
    void equalsIgnoresInsignificantTrailingZeros() {
        Amount firstAmount = new Amount(900.0);
        Amount secondAmount = new Amount(900.00);

        assertEquals(firstAmount, secondAmount);
        assertEquals(firstAmount.hashCode(), secondAmount.hashCode());
    }

    @Test
    void equalsReturnsFalseForDifferentAmounts() {
        Amount firstAmount = new Amount(900.0);
        Amount secondAmount = new Amount(901.0);

        assertNotEquals(firstAmount, secondAmount);
    }
}
