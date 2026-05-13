package se.kth.iv1350.repairebike.integration;

/**
 * Thrown when no customer exists for a searched phone number.
 */
public class NoSuchCustomerException extends Exception {
    private final String searchedPhoneNumber;

    /**
     * Creates a new exception for a missing customer.
     *
     * @param searchedPhoneNumber The phone number that did not match any customer.
     */
    public NoSuchCustomerException(String searchedPhoneNumber) {
        super("No customer exists with phone number: " + searchedPhoneNumber);
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
