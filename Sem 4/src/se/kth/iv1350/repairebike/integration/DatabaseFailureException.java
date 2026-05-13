package se.kth.iv1350.repairebike.integration;

/**
 * Thrown when the customer database can not be reached.
 */
public class DatabaseFailureException extends Exception {
    private final String searchedPhoneNumber;

    /**
     * Creates a new exception for a database failure.
     *
     * @param searchedPhoneNumber The phone number that was searched when the database failed.
     */
    public DatabaseFailureException(String searchedPhoneNumber) {
        super("Could not access the customer database while searching for phone number: " + searchedPhoneNumber);
        this.searchedPhoneNumber = searchedPhoneNumber;
    }

    /**
     * Gets the phone number that was searched when the database failed.
     *
     * @return The searched phone number.
     */
    public String getSearchedPhoneNumber() {
        return searchedPhoneNumber;
    }
}
