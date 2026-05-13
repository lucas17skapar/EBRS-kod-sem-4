package se.kth.iv1350.repairebike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;

/**
 * Stores customer data in memory.
 */
public class CustomerRegistry {
    private final List<Customer> customers = new ArrayList<>();

    /**
     * Creates a customer registry with hard-coded customers.
     */
    public CustomerRegistry() {
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
     * @return The matching customer, or {@code null} if no match is found.
     */
    public Customer findCustomerByPhoneNumber(String phoneNumber) {
        for (Customer customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }
}
