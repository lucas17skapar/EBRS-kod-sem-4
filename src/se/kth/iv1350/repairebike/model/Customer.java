package se.kth.iv1350.repairebike.model;

/**
 * Represents a customer.
 */
public class Customer {
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final Bike bike;

    /**
     * Creates a customer.
     *
     * @param name The customer name.
     * @param phoneNumber The phone number.
     * @param email The email address.
     * @param bike The customer's bike.
     */
    public Customer(String name, String phoneNumber, String email, Bike bike) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.bike = bike;
    }

    /**
     * Gets the customer name.
     *
     * @return The customer name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the phone number.
     *
     * @return The phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the email address.
     *
     * @return The email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the bike.
     *
     * @return The bike.
     */
    public Bike getBike() {
        return bike;
    }

    /**
     * Returns a string representation of the customer.
     *
     * @return A customer string.
     */
    @Override
    public String toString() {
        return "Customer{name='" + name + "', phoneNumber='" + phoneNumber + "', email='" + email + "', bike=" + bike + "}";
    }
}
