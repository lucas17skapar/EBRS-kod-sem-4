package se.kth.iv1350.repairebike.view;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import se.kth.iv1350.repairebike.model.Amount;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.DiagnosticReport;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot;
import se.kth.iv1350.repairebike.model.RepairTask;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepairOrderLoggerTest {
    @Test
    void repairOrderUpdatedWritesOneLogEntryForUpdate() {
        StringWriter log = new StringWriter();
        RepairOrderLogger repairOrderLogger = new RepairOrderLogger(new PrintWriter(log));
        RepairOrderSnapshot snapshot = createRepairOrderSnapshot();

        repairOrderLogger.repairOrderUpdated(snapshot);

        assertTrue(log.toString().contains("Repair order update written at"));
        assertTrue(log.toString().contains("RepairOrder{orderId=1"));
        assertTrue(log.toString().contains("Connector issue."));
        assertTrue(log.toString().contains("phoneNumber='****4567'"));
        assertTrue(log.toString().contains("email='s***@example.com'"));
    }

    private RepairOrderSnapshot createRepairOrderSnapshot() {
        Bike bike = new Bike("Crescent", "Elina", "SN-12345");
        Customer customer = new Customer("Sara Lind", "0701234567", "sara.lind@example.com", bike);
        RepairOrder repairOrder = new RepairOrder(1, customer, bike, "Battery drains quickly.");
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace connector", new Amount(900.0)));
        repairOrder.addDiagnosticReportAndProposedRepairTasks(
            new DiagnosticReport("Connector issue."),
            tasks
        );
        return new RepairOrderSnapshot(repairOrder, repairOrder.calculateTotalCost());
    }
}
