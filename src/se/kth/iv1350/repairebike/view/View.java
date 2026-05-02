package se.kth.iv1350.repairebike.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.controller.Controller;
import se.kth.iv1350.repairebike.model.Amount;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairTask;

/**
 * The console view with a hard-coded sample flow.
 */
public class View {
    private final Controller controller;

    /**
     * Creates a new view.
     *
     * @param controller The controller used by this view.
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Runs the seminar 3 basic flow.
     */
    public void sampleExecution() {
        Customer customer = controller.findCustomer("0701234567");
        System.out.println("1. Found customer: " + customer);

        if (customer != null) {
            System.out.println("2. Found bike: " + customer.getBike());
        } else {
            System.out.println("2. Found bike: none");
        }

        RepairOrder createdOrder = controller.createRepairOrder("Battery drains quickly and motor cuts out.");
        System.out.println("3-4. Created repair order: " + createdOrder);
        int repairOrderId = -1;
        if (createdOrder != null) {
            repairOrderId = createdOrder.getOrderId();
        }

        List<RepairTask> repairTasks = new ArrayList<>();
        repairTasks.add(new RepairTask("Replace battery connector", new Amount(900.0)));
        repairTasks.add(new RepairTask("Update motor controller firmware", new Amount(650.0)));
        System.out.println("5. Repair tasks: " + repairTasks);

        LocalDate estimatedCompletionDate = LocalDate.now().plusDays(3);
        if (createdOrder != null) {
            estimatedCompletionDate = createdOrder.getCreatedDate().plusDays(3);
        }

        RepairOrder updatedOrder = controller.addDiagnosticReport(
            repairOrderId,
            "Loose battery connector found and outdated firmware detected.",
            repairTasks,
            estimatedCompletionDate
        );
        System.out.println("6-7. Updated repair order: " + updatedOrder);

        if (updatedOrder != null) {
            System.out.println("7. Total cost: " + updatedOrder.calculateTotalCost());
        } else {
            System.out.println("7. Total cost: no matching repair order");
        }

        RepairOrder orderForApproval = controller.prepareRepairOrderForApproval(repairOrderId);
        System.out.println("8. Repair order for approval: " + orderForApproval);

        System.out.println("9. Printer output:");
        RepairOrder acceptedOrder = controller.acceptRepairOrder(repairOrderId);

        System.out.println("10. Accepted repair order: " + acceptedOrder);
    }
}
