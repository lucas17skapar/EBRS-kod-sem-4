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

    /**
     * Returns a string representation of the amount.
     *
     * @return An amount string.
     */
    @Override
    public String toString() {
        return value.stripTrailingZeros().toPlainString();
    }
}
