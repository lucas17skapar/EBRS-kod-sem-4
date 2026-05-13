package se.kth.iv1350.repairebike.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.model.Customer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class CustomerRegistryTest {
    private CustomerRegistry customerRegistry;

    @BeforeEach
    void setUp() {
        customerRegistry = new CustomerRegistry();
    }

    @Test
    void findCustomerByPhoneNumberReturnsMatchingCustomer() {
        Customer result = customerRegistry.findCustomerByPhoneNumber("0701234567");

        assertNotNull(result);
        assertEquals("Sara Lind", result.getName());
        assertEquals("0701234567", result.getPhoneNumber());
    }

    @Test
    void findCustomerByPhoneNumberReturnsNullForMissingCustomer() {
        Customer result = customerRegistry.findCustomerByPhoneNumber("0000000000");

        assertNull(result);
    }
}
