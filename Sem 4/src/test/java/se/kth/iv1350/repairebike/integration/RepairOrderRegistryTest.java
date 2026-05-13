package se.kth.iv1350.repairebike.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.RepairOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

class RepairOrderRegistryTest {
    private RepairOrderRegistry repairOrderRegistry;

    @BeforeEach
    void setUp() {
        repairOrderRegistry = new RepairOrderRegistry();
    }

    @Test
    void findRepairOrderReturnsMatchingRepairOrder() {
        RepairOrder firstOrder = createRepairOrder(1, "First issue.");
        RepairOrder secondOrder = createRepairOrder(2, "Second issue.");

        repairOrderRegistry.addRepairOrder(firstOrder);
        repairOrderRegistry.addRepairOrder(secondOrder);
        RepairOrder result = repairOrderRegistry.findRepairOrder(1);

        assertSame(firstOrder, result);
    }

    @Test
    void findRepairOrderReturnsNullWhenNoRepairOrderMatchesId() {
        RepairOrder repairOrder = createRepairOrder(1, "First issue.");

        repairOrderRegistry.addRepairOrder(repairOrder);
        RepairOrder result = repairOrderRegistry.findRepairOrder(2);

        assertNull(result);
    }

    @Test
    void generateRepairOrderIdReturnsUniqueIds() {
        int firstId = repairOrderRegistry.generateRepairOrderId();
        int secondId = repairOrderRegistry.generateRepairOrderId();

        assertEquals(1, firstId);
        assertEquals(2, secondId);
    }

    private RepairOrder createRepairOrder(int orderId, String problemDescription) {
        Bike bike = new Bike("Crescent", "Elina", "SN-12345");
        Customer customer = new Customer("Sara Lind", "0701234567", "sara.lind@example.com", bike);
        return new RepairOrder(orderId, customer, bike, problemDescription);
    }
}
