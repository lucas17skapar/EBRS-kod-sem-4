package se.kth.iv1350.repairebike.view;

import java.util.List;
import java.math.BigDecimal;
import se.kth.iv1350.repairebike.dto.BikeDTO;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot.BikeData;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot.CustomerData;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot.RepairTaskData;

class RepairOrderFormatter {
    private RepairOrderFormatter() {
    }

    static String formatRepairOrder(RepairOrderDTO repairOrder) {
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

    static String formatRepairOrder(RepairOrderSnapshot repairOrder) {
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
            + ", repairTasks=" + formatSnapshotRepairTasks(repairOrder.getRepairTasks())
            + ", state=" + repairOrder.getState()
            + ", estimatedCompletionDate=" + repairOrder.getEstimatedCompletionDate()
            + ", totalCost=" + formatAmount(repairOrder.getTotalCost())
            + "}";
    }

    static String formatRepairOrderForLog(RepairOrderSnapshot repairOrder) {
        if (repairOrder == null) {
            return "none";
        }

        return "RepairOrder{"
            + "orderId=" + repairOrder.getOrderId()
            + ", customer=" + formatCustomerForLog(repairOrder.getCustomer())
            + ", bikeSerialNumber=" + repairOrder.getBike().serialNumber()
            + ", problemDescription='" + repairOrder.getProblemDescription() + "'"
            + ", diagnosticReportText='" + formatNullable(repairOrder.getDiagnosticReportText()) + "'"
            + ", state=" + repairOrder.getState()
            + ", estimatedCompletionDate=" + repairOrder.getEstimatedCompletionDate()
            + ", totalCost=" + formatAmount(repairOrder.getTotalCost())
            + "}";
    }

    static String formatRepairOrderReceipt(RepairOrderDTO repairOrder) {
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

    static String formatCustomer(CustomerDTO customer) {
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

    static String formatCustomer(CustomerData customer) {
        if (customer == null) {
            return "none";
        }

        return "Customer{"
            + "name='" + customer.name() + "'"
            + ", phoneNumber='" + customer.phoneNumber() + "'"
            + ", email='" + customer.email() + "'"
            + "}";
    }

    private static String formatCustomerForLog(CustomerData customer) {
        if (customer == null) {
            return "none";
        }

        return "Customer{"
            + "name='hidden'"
            + ", phoneNumber='" + maskPhoneNumber(customer.phoneNumber()) + "'"
            + ", email='" + maskEmail(customer.email()) + "'"
            + "}";
    }

    static String formatBike(BikeDTO bike) {
        if (bike == null) {
            return "none";
        }

        return "Bike{"
            + "brand='" + bike.getBrand() + "'"
            + ", model='" + bike.getModel() + "'"
            + ", serialNumber='" + bike.getSerialNumber() + "'"
            + "}";
    }

    static String formatBike(BikeData bike) {
        if (bike == null) {
            return "none";
        }

        return "Bike{"
            + "brand='" + bike.brand() + "'"
            + ", model='" + bike.model() + "'"
            + ", serialNumber='" + bike.serialNumber() + "'"
            + ", warrantyEndDate=" + bike.warrantyEndDate()
            + "}";
    }

    static String formatRepairTasks(List<RepairTaskDTO> repairTasks) {
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

    private static String formatSnapshotRepairTasks(List<RepairTaskData> repairTasks) {
        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < repairTasks.size(); i++) {
            RepairTaskData repairTask = repairTasks.get(i);
            builder.append("RepairTask{")
                .append("description='").append(repairTask.description()).append("'")
                .append(", cost=").append(formatAmount(repairTask.cost()))
                .append("}");
            if (i < repairTasks.size() - 1) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    static String formatAmount(double amount) {
        if (amount == Math.rint(amount)) {
            return Long.toString(Math.round(amount));
        }
        return Double.toString(amount);
    }

    static String formatAmount(BigDecimal amount) {
        return amount.stripTrailingZeros().toPlainString();
    }

    private static String formatNullable(String text) {
        if (text == null) {
            return "none";
        }
        return text;
    }

    private static String maskPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 4) {
            return "****";
        }
        return "****" + phoneNumber.substring(phoneNumber.length() - 4);
    }

    private static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return "hidden";
        }
        String[] parts = email.split("@", 2);
        if (parts[0].isEmpty()) {
            return "***@" + parts[1];
        }
        return parts[0].charAt(0) + "***@" + parts[1];
    }
}
