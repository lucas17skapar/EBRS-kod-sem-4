package se.kth.iv1350.repairebike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;

/**
 * Stores customer data in memory.
 */
public class CustomerRegistry {
    /**
     * Searching for this phone number simulates a database failure.
     */
    public static final String DATABASE_FAILURE_PHONE_NUMBER = "9999999999";

    private static final CustomerRegistry INSTANCE = new CustomerRegistry();

    private final List<Customer> customers = new ArrayList<>();

    /**
     * Gets the only customer registry instance.
     *
     * @return The customer registry instance.
     */
    public static CustomerRegistry getInstance() {
        return INSTANCE;
    }

    private CustomerRegistry() {
        customers.add(
            new Customer(
                "Sara Lind",
                "0701234567",
                "sara.lind@example.com",
                new Bike("Crescent", "Elina", "SN-12345")
            )
        );
        customers.add(
            new Customer(
                "Adam Holm",
                "0709876543",
                "adam.holm@example.com",
                new Bike("Monark", "E-City", "SN-67890")
            )
        );
    }

    /**
     * Finds a customer by phone number.
     *
     * @param phoneNumber The phone number to search for.
     * @return The matching customer.
     * @throws NoSuchCustomerException If no customer has the specified phone number.
     * @throws DatabaseFailureException If the customer database can not be reached.
     */
    public Customer findCustomerByPhoneNumber(String phoneNumber)
        throws NoSuchCustomerException, DatabaseFailureException {
        if (DATABASE_FAILURE_PHONE_NUMBER.equals(phoneNumber)) {
            throw new DatabaseFailureException(phoneNumber);
        }

        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        throw new NoSuchCustomerException(phoneNumber);
    }
}
