package se.kth.iv1350.repairebike.view;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.controller.Controller;
import se.kth.iv1350.repairebike.dto.BikeDTO;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;

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
        CustomerDTO customer = controller.findCustomer("0701234567");
        System.out.println("1. Found customer: " + formatCustomer(customer));

        if (customer != null) {
            System.out.println("2. Found bike: " + formatBike(customer.getBike()));
        } else {
            System.out.println("2. Found bike: none");
        }

        RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly and motor cuts out.");
        System.out.println("3-4. Created repair order: " + formatRepairOrder(createdOrder));
        int repairOrderId = -1;
        if (createdOrder != null) {
            repairOrderId = createdOrder.getOrderId();
        }

        List<RepairTaskDTO> repairTasks = new ArrayList<>();
        repairTasks.add(new RepairTaskDTO("Replace battery connector", 900.0));
        repairTasks.add(new RepairTaskDTO("Update motor controller firmware", 650.0));
        System.out.println("5. Repair tasks: " + formatRepairTasks(repairTasks));

        LocalDate estimatedCompletionDate = LocalDate.now().plusDays(3);
        if (createdOrder != null) {
            estimatedCompletionDate = createdOrder.getCreatedDate().plusDays(3);
        }

        RepairOrderDTO updatedOrder = controller.addDiagnosticReport(
            repairOrderId,
            "Loose battery connector found and outdated firmware detected.",
            repairTasks,
            estimatedCompletionDate
        );
        System.out.println("6-7. Updated repair order: " + formatRepairOrder(updatedOrder));

        if (updatedOrder != null) {
            System.out.println("7. Total cost: " + formatAmount(updatedOrder.getTotalCost()));
        } else {
            System.out.println("7. Total cost: no matching repair order");
        }

        RepairOrderDTO orderForApproval = controller.prepareRepairOrderForApproval(repairOrderId);
        System.out.println("8. Repair order for approval: " + formatRepairOrder(orderForApproval));

        System.out.println("9. Printer output:");
        RepairOrderDTO acceptedOrder = controller.acceptRepairOrder(repairOrderId);
        if (acceptedOrder != null && "ACCEPTED".equals(acceptedOrder.getState())) {
            controller.printRepairOrder(formatRepairOrderReceipt(acceptedOrder));
        }

        System.out.println("10. Accepted repair order: " + formatRepairOrder(acceptedOrder));
    }

    private String formatRepairOrder(RepairOrderDTO repairOrder) {
        if (repairOrder == null) {
            return "none";
        }

        return "RepairOrder{"
            + "orderId=" + repairOrder.getOrderId()
            + ", customer=" + formatCustomer(repairOrder.getCustomer())
            + ", bike=" + formatBike(repairOrder.getBike())
            + ", problemDescription='" + repairOrder.getProblemDescription() + "'"
            + ", createdDate=" + repairOrder.getCreatedDate()
            + ", diagnosticReportText='" + formatNullable(repairOrder.getDiagnosticReportText()) + "'"
            + ", repairTasks=" + formatRepairTasks(repairOrder.getRepairTasks())
            + ", state=" + repairOrder.getState()
            + ", estimatedCompletionDate=" + repairOrder.getEstimatedCompletionDate()
            + ", totalCost=" + formatAmount(repairOrder.getTotalCost())
            + "}";
    }

    private String formatRepairOrderReceipt(RepairOrderDTO repairOrder) {
        String lineSeparator = System.lineSeparator();
        StringBuilder builder = new StringBuilder();
        builder.append("Repair Order").append(lineSeparator);
        builder.append("Order ID: ").append(repairOrder.getOrderId()).append(lineSeparator);
        builder.append("State: ").append(repairOrder.getState()).append(lineSeparator);
        builder.append("Created Date: ").append(repairOrder.getCreatedDate()).append(lineSeparator);
        builder.append("Estimated Completion Date: ")
            .append(repairOrder.getEstimatedCompletionDate())
            .append(lineSeparator);
        builder.append("Customer: ").append(formatCustomer(repairOrder.getCustomer())).append(lineSeparator);
        builder.append("Bike: ").append(formatBike(repairOrder.getBike())).append(lineSeparator);
        builder.append("Problem Description: ")
            .append(repairOrder.getProblemDescription())
            .append(lineSeparator);
        builder.append("Diagnostic Report: ")
            .append(formatNullable(repairOrder.getDiagnosticReportText()))
            .append(lineSeparator);
        builder.append("Repair Tasks: ")
            .append(formatRepairTasks(repairOrder.getRepairTasks()))
            .append(lineSeparator);
        builder.append("Total Cost: ")
            .append(formatAmount(repairOrder.getTotalCost()))
            .append(lineSeparator);
        return builder.toString();
    }

    private String formatCustomer(CustomerDTO customer) {
        if (customer == null) {
            return "none";
        }

        return "Customer{"
            + "name='" + customer.getName() + "'"
            + ", phoneNumber='" + customer.getPhoneNumber() + "'"
            + ", email='" + customer.getEmail() + "'"
            + ", bike=" + formatBike(customer.getBike())
            + "}";
    }

    private String formatBike(BikeDTO bike) {
        if (bike == null) {
            return "none";
        }

        return "Bike{"
            + "brand='" + bike.getBrand() + "'"
            + ", model='" + bike.getModel() + "'"
            + ", serialNumber='" + bike.getSerialNumber() + "'"
            + "}";
    }

    private String formatRepairTasks(List<RepairTaskDTO> repairTasks) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < repairTasks.size(); i++) {
            RepairTaskDTO repairTask = repairTasks.get(i);
            builder.append("RepairTask{")
                .append("description='").append(repairTask.getDescription()).append("'")
                .append(", cost=").append(formatAmount(repairTask.getCost()))
                .append("}");
            if (i < repairTasks.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    private String formatAmount(double amount) {
        if (amount == Math.rint(amount)) {
            return Long.toString(Math.round(amount));
        }
        return Double.toString(amount);
    }

    private String formatNullable(String text) {
        if (text == null) {
            return "none";
        }
        return text;
    }
}
