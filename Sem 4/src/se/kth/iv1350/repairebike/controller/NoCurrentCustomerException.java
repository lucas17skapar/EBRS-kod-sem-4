package se.kth.iv1350.repairebike.controller;

/**
 * Thrown when an operation requires a selected customer, but no customer has been selected.
 */
public class NoCurrentCustomerException extends Exception {
    /**
     * Creates a new exception for a missing current customer.
     */
    public NoCurrentCustomerException() {
        super("No customer has been selected.");
    }
}
