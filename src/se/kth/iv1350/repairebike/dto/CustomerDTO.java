package se.kth.iv1350.repairebike.dto;

/**
 * Contains customer data that may be shown by the view.
 */
public class CustomerDTO {
    private final String name;
    private final String phoneNumber;
    private final String email;
    private final BikeDTO bike;

    /**
     * Creates a new customer DTO.
     *
     * @param name The customer name.
     * @param phoneNumber The customer phone number.
     * @param email The customer email address.
     * @param bike The customer's bike.
     */
    public CustomerDTO(String name, String phoneNumber, String email, BikeDTO bike) {
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
     * Gets the customer phone number.
     *
     * @return The customer phone number.
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Gets the customer email address.
     *
     * @return The customer email address.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Gets the customer's bike.
     *
     * @return The customer's bike.
     */
    public BikeDTO getBike() {
        return bike;
    }
}
