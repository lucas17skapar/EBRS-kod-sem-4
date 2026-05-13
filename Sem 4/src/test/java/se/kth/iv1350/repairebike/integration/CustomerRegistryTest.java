package se.kth.iv1350.repairebike.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.model.Customer;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerRegistryTest {
    private CustomerRegistry customerRegistry;

    @BeforeEach
    void setUp() {
        customerRegistry = CustomerRegistry.getInstance();
    }

    @Test
    void findCustomerByPhoneNumberReturnsMatchingCustomer() throws Exception {
        Customer result = customerRegistry.findCustomerByPhoneNumber("0701234567");

        assertNotNull(result);
        assertEquals("Sara Lind", result.getName());
        assertEquals("0701234567", result.getPhoneNumber());
    }

    @Test
    void findCustomerByPhoneNumberThrowsExceptionForMissingCustomer() {
        NoSuchCustomerException exception = assertThrows(
            NoSuchCustomerException.class,
            () -> customerRegistry.findCustomerByPhoneNumber("0000000000")
        );

        assertEquals("0000000000", exception.getSearchedPhoneNumber());
    }

    @Test
    void findCustomerByPhoneNumberThrowsExceptionWhenDatabaseFails() {
        DatabaseFailureException exception = assertThrows(
            DatabaseFailureException.class,
            () -> customerRegistry.findCustomerByPhoneNumber(CustomerRegistry.DATABASE_FAILURE_PHONE_NUMBER)
        );

        assertEquals(CustomerRegistry.DATABASE_FAILURE_PHONE_NUMBER, exception.getSearchedPhoneNumber());
    }
}
