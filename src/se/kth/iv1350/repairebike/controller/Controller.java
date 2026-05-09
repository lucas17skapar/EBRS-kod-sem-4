package se.kth.iv1350.repairebike.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.dto.BikeDTO;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairebike.model.Amount;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.DiagnosticReport;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairTask;

/**
 * The Controller coordinates calls between the MVC layers view, integration and model.
 */
public class Controller {
    private final CustomerRegistry customerRegistry;
    private final RepairOrderRegistry repairOrderRegistry;
    private final Printer printer;
    private Customer currentCustomer;

    /**
     * Create a controller with required dependencies.
     *
     * @param customerRegistry The customer registry.
     * @param repairOrderRegistry The repair order registry.
     * @param printer The printer.
     */
    public Controller(
        CustomerRegistry customerRegistry,
        RepairOrderRegistry repairOrderRegistry,
        Printer printer
    ) {
        this.customerRegistry = customerRegistry;
        this.repairOrderRegistry = repairOrderRegistry;
        this.printer = printer;
    }

    /**
     * Finds a customer by phone number.
     *
     * @param phoneNumber The customer's phone number.
     * @return The found customer data, or {@code null} if not found.
     */
    public CustomerDTO findCustomer(String phoneNumber) {
        currentCustomer = customerRegistry.findCustomerByPhoneNumber(phoneNumber);
        return createCustomerDTO(currentCustomer);
    }

    /**
     * Creates a new repair order for the current customer.
     *
     * @param problemDescription The customer's problem description.
     * @return The created repair order data, or {@code null} if no customer is selected.
     */
    public RepairOrderDTO createRepairOrder(String problemDescription) {
        if (currentCustomer == null) {
            return null;
        }

        int repairOrderId = repairOrderRegistry.generateRepairOrderId();
        RepairOrder repairOrder = new RepairOrder(
            repairOrderId,
            currentCustomer,
            currentCustomer.getBike(),
            problemDescription
        );
        repairOrderRegistry.addRepairOrder(repairOrder);
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Finds a repair order by id.
     *
     * @param repairOrderId The repair order id.
     * @return The matching repair order data, or {@code null} if no match is found.
     */
    public RepairOrderDTO findRepairOrder(int repairOrderId) {
        return createRepairOrderDTO(repairOrderRegistry.findRepairOrder(repairOrderId));
    }

    /**
     * Adds a diagnostic report and proposed repair tasks to a repair order.
     *
     * @param repairOrderId The repair order id.
     * @param reportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @return The updated repair order data, or {@code null} if no matching order exists.
     */
    public RepairOrderDTO addDiagnosticReport(
        int repairOrderId,
        String reportText,
        List<RepairTaskDTO> repairTasks
    ) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        return addDiagnosticReportToRepairOrder(
            repairOrder,
            reportText,
            createRepairTasks(repairTasks),
            repairOrder.getCreatedDate().plusDays(3)
        );
    }

    /**
     * Adds a diagnostic report, proposed repair tasks, and estimated completion date to a repair order.
     *
     * @param repairOrderId The repair order id.
     * @param reportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @param estimatedCompletionDate The estimated completion date.
     * @return The updated repair order data, or {@code null} if no matching order exists.
     */
    public RepairOrderDTO addDiagnosticReport(
        int repairOrderId,
        String reportText,
        List<RepairTaskDTO> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        return addDiagnosticReportToRepairOrder(
            repairOrder,
            reportText,
            createRepairTasks(repairTasks),
            estimatedCompletionDate
        );
    }

    /**
     * Prepares a repair order for customer approval.
     *
     * @param repairOrderId The repair order id.
     * @return The prepared repair order data, or {@code null} if no matching order exists.
     */
    public RepairOrderDTO prepareRepairOrderForApproval(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        repairOrder.prepareRepairOrderForApproval();
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Accepts a repair order.
     *
     * @param repairOrderId The repair order id.
     * @return The accepted repair order data, or {@code null} if no matching order exists.
     */
    public RepairOrderDTO acceptRepairOrder(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        repairOrder.acceptRepairOrder();
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Rejects a repair order.
     *
     * @param repairOrderId The repair order id.
     * @return The rejected repair order data, or {@code null} if no matching order exists.
     */
    public RepairOrderDTO rejectRepairOrder(int repairOrderId) {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            return null;
        }

        repairOrder.rejectRepairOrder();
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Prints a formatted repair order.
     *
     * @param printableRepairOrder The formatted repair order to print.
     */
    public void printRepairOrder(String printableRepairOrder) {
        printer.printRepairOrder(printableRepairOrder);
    }

    private RepairOrderDTO addDiagnosticReportToRepairOrder(
        RepairOrder repairOrder,
        String reportText,
        List<RepairTask> repairTasks,
        LocalDate estimatedCompletionDate
    ) {
        DiagnosticReport diagnosticReport = new DiagnosticReport(reportText);
        repairOrder.addDiagnosticReportAndProposedRepairTasks(
            diagnosticReport,
            repairTasks,
            estimatedCompletionDate
        );
        return createRepairOrderDTO(repairOrder);
    }

    private List<RepairTask> createRepairTasks(List<RepairTaskDTO> repairTaskDTOs) {
        List<RepairTask> repairTasks = new ArrayList<>();
        for (RepairTaskDTO repairTaskDTO : repairTaskDTOs) {
            repairTasks.add(
                new RepairTask(
                    repairTaskDTO.getDescription(),
                    new Amount(repairTaskDTO.getCost())
                )
            );
        }
        return repairTasks;
    }

    private RepairOrderDTO createRepairOrderDTO(RepairOrder repairOrder) {
        if (repairOrder == null) {
            return null;
        }

        String diagnosticReportText = null;
        if (repairOrder.getDiagnosticReport() != null) {
            diagnosticReportText = repairOrder.getDiagnosticReport().getReportText();
        }

        return new RepairOrderDTO(
            repairOrder.getOrderId(),
            createCustomerDTO(repairOrder.getCustomer()),
            createBikeDTO(repairOrder.getBike()),
            repairOrder.getProblemDescription(),
            repairOrder.getCreatedDate(),
            diagnosticReportText,
            createRepairTaskDTOs(repairOrder.getRepairTasks()),
            repairOrder.getState().name(),
            repairOrder.getEstimatedCompletionDate(),
            repairOrder.calculateTotalCost().getValue()
        );
    }

    private CustomerDTO createCustomerDTO(Customer customer) {
        if (customer == null) {
            return null;
        }

        return new CustomerDTO(
            customer.getName(),
            customer.getPhoneNumber(),
            customer.getEmail(),
            createBikeDTO(customer.getBike())
        );
    }

    private BikeDTO createBikeDTO(Bike bike) {
        if (bike == null) {
            return null;
        }

        return new BikeDTO(
            bike.getBrand(),
            bike.getModel(),
            bike.getSerialNumber()
        );
    }

    private List<RepairTaskDTO> createRepairTaskDTOs(List<RepairTask> repairTasks) {
        List<RepairTaskDTO> repairTaskDTOs = new ArrayList<>();
        for (RepairTask repairTask : repairTasks) {
            repairTaskDTOs.add(
                new RepairTaskDTO(
                    repairTask.getDescription(),
                    repairTask.getCost().getValue()
                )
            );
        }
        return repairTaskDTOs;
    }

}
