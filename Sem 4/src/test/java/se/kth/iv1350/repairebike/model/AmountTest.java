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
    void subtractReturnsDifferenceBetweenTwoAmounts() {
        Amount firstAmount = new Amount(900.50);
        Amount secondAmount = new Amount(400.25);

        Amount result = firstAmount.subtract(secondAmount);

        assertEquals(new Amount(500.25), result);
    }

    @Test
    void multiplyReturnsProductOfAmountAndFactor() {
        Amount amount = new Amount(1550.0);

        Amount result = amount.multiply(0.10);

        assertEquals(new Amount(155.0), result);
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
