package se.kth.iv1350.repairebike.model;

import java.math.BigDecimal;

/**
 * Represents an amount of money.
 */
public class Amount {
    private final BigDecimal value;

    /**
     * Creates a new amount.
     *
     * @param value The amount value.
     */
    public Amount(double value) {
        this(BigDecimal.valueOf(value));
    }

    /**
     * Creates a new amount.
     *
     * @param value The amount value.
     */
    public Amount(BigDecimal value) {
        this.value = value;
    }

    /**
     * Adds another amount to this amount.
     *
     * @param other The amount to add.
     * @return The sum of both amounts.
     */
    public Amount add(Amount other) {
        return new Amount(value.add(other.value));
    }

    /**
     * Subtracts another amount from this amount.
     *
     * @param other The amount to subtract.
     * @return The difference between both amounts.
     */
    public Amount subtract(Amount other) {
        return new Amount(value.subtract(other.value));
    }

    /**
     * Multiplies this amount by the specified factor.
     *
     * @param factor The factor to multiply by.
     * @return The multiplied amount.
     */
    public Amount multiply(double factor) {
        return new Amount(value.multiply(BigDecimal.valueOf(factor)));
    }

    /**
     * Gets the amount value.
     *
     * @return The amount as a double.
     */
    public double getValue() {
        return value.doubleValue();
    }

    /**
     * Checks if another object is equal to this amount.
     *
     * @param other The object to compare with.
     * @return {@code true} if both amounts have the same value, otherwise {@code false}.
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof Amount)) {
            return false;
        }
        Amount otherAmount = (Amount) other;
        return value.compareTo(otherAmount.value) == 0;
    }

    /**
     * Gets the hash code for this amount.
     *
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return value.stripTrailingZeros().hashCode();
    }

}
