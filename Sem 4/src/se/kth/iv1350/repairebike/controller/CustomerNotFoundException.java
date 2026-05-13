package se.kth.iv1350.repairebike.controller;

/**
 * Thrown when the application can not find a customer for a searched phone number.
 */
public class CustomerNotFoundException extends Exception {
    private final String searchedPhoneNumber;

    /**
     * Creates a new exception for a missing customer.
     *
     * @param searchedPhoneNumber The phone number that did not match any customer.
     * @param cause The lower-level exception that caused this exception.
     */
    public CustomerNotFoundException(String searchedPhoneNumber, Exception cause) {
        super("No customer found for phone number: " + searchedPhoneNumber, cause);
        this.searchedPhoneNumber = searchedPhoneNumber;
    }

    /**
     * Gets the phone number that did not match any customer.
     *
     * @return The searched phone number.
     */
    public String getSearchedPhoneNumber() {
        return searchedPhoneNumber;
    }
}
