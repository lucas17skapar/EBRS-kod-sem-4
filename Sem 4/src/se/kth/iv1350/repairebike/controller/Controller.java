package se.kth.iv1350.repairebike.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.dto.BikeDTO;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;
import se.kth.iv1350.repairebike.integration.CustomerRegistry;
import se.kth.iv1350.repairebike.integration.DatabaseFailureException;
import se.kth.iv1350.repairebike.integration.NoSuchCustomerException;
import se.kth.iv1350.repairebike.integration.Printer;
import se.kth.iv1350.repairebike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairebike.model.Amount;
import se.kth.iv1350.repairebike.model.Bike;
import se.kth.iv1350.repairebike.model.Customer;
import se.kth.iv1350.repairebike.model.DiagnosticReport;
import se.kth.iv1350.repairebike.model.DiscountStrategy;
import se.kth.iv1350.repairebike.model.NoDiscountStrategy;
import se.kth.iv1350.repairebike.model.RepairOrder;
import se.kth.iv1350.repairebike.model.RepairOrderObserver;
import se.kth.iv1350.repairebike.model.RepairTask;

/**
 * The Controller coordinates calls between the MVC layers view, integration and model.
 */
public class Controller {
    private final CustomerRegistry customerRegistry;
    private final RepairOrderRegistry repairOrderRegistry;
    private final Printer printer;
    private final DiscountStrategy discountStrategy;
    private final List<RepairOrderObserver> repairOrderObservers = new ArrayList<>();
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
        this(customerRegistry, repairOrderRegistry, printer, new NoDiscountStrategy());
    }

    /**
     * Create a controller with required dependencies and a discount strategy.
     *
     * @param customerRegistry The customer registry.
     * @param repairOrderRegistry The repair order registry.
     * @param printer The printer.
     * @param discountStrategy The strategy used to calculate repair order discounts.
     */
    public Controller(
        CustomerRegistry customerRegistry,
        RepairOrderRegistry repairOrderRegistry,
        Printer printer,
        DiscountStrategy discountStrategy
    ) {
        this.customerRegistry = customerRegistry;
        this.repairOrderRegistry = repairOrderRegistry;
        this.printer = printer;
        this.discountStrategy = discountStrategy;
    }

    /**
     * Adds an observer that will be notified when repair orders are updated.
     *
     * @param repairOrderObserver The observer to notify.
     */
    public void addRepairOrderObserver(RepairOrderObserver repairOrderObserver) {
        repairOrderObservers.add(repairOrderObserver);
    }

    /**
     * Finds a customer by phone number.
     *
     * @param phoneNumber The customer's phone number.
     * @return The found customer data.
     * @throws CustomerNotFoundException If no customer has the specified phone number.
     * @throws OperationFailedException If the customer search can not be completed.
     */
    public CustomerDTO findCustomer(String phoneNumber)
        throws CustomerNotFoundException, OperationFailedException {
        try {
            Customer foundCustomer = customerRegistry.findCustomerByPhoneNumber(phoneNumber);
            currentCustomer = foundCustomer;
            return createCustomerDTO(foundCustomer);
        } catch (NoSuchCustomerException exc) {
            throw new CustomerNotFoundException(phoneNumber, exc);
        } catch (DatabaseFailureException exc) {
            throw new OperationFailedException("Could not search for customer.", exc);
        }
    }

    /**
     * Creates a new repair order for the current customer.
     *
     * @param problemDescription The customer's problem description.
     * @return The created repair order data.
     * @throws NoCurrentCustomerException If no customer has been selected.
     */
    public RepairOrderDTO createRepairOrder(String problemDescription) throws NoCurrentCustomerException {
        if (currentCustomer == null) {
            throw new NoCurrentCustomerException();
        }

        int repairOrderId = repairOrderRegistry.generateRepairOrderId();
        RepairOrder repairOrder = new RepairOrder(
            repairOrderId,
            currentCustomer,
            currentCustomer.getBike(),
            problemDescription,
            discountStrategy,
            repairOrderObservers
        );
        repairOrderRegistry.addRepairOrder(repairOrder);
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Finds a repair order by id.
     *
     * @param repairOrderId The repair order id.
     * @return The matching repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO findRepairOrder(int repairOrderId) throws RepairOrderNotFoundException {
        return createRepairOrderDTO(findExistingRepairOrder(repairOrderId));
    }

    /**
     * Adds a diagnostic report and proposed repair tasks to a repair order.
     *
     * @param repairOrderId The repair order id.
     * @param reportText The diagnostic report text.
     * @param repairTasks The proposed repair tasks.
     * @return The updated repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO addDiagnosticReport(
        int repairOrderId,
        String reportText,
        List<RepairTaskDTO> repairTasks
    ) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = findExistingRepairOrder(repairOrderId);
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
     * @return The updated repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO addDiagnosticReport(
        int repairOrderId,
        String reportText,
        List<RepairTaskDTO> repairTasks,
        LocalDate estimatedCompletionDate
    ) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = findExistingRepairOrder(repairOrderId);
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
     * @return The prepared repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO prepareRepairOrderForApproval(int repairOrderId) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = findExistingRepairOrder(repairOrderId);
        repairOrder.prepareRepairOrderForApproval();
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Accepts a repair order.
     *
     * @param repairOrderId The repair order id.
     * @return The accepted repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO acceptRepairOrder(int repairOrderId) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = findExistingRepairOrder(repairOrderId);
        repairOrder.acceptRepairOrder();
        return createRepairOrderDTO(repairOrder);
    }

    /**
     * Rejects a repair order.
     *
     * @param repairOrderId The repair order id.
     * @return The rejected repair order data.
     * @throws RepairOrderNotFoundException If no repair order has the specified id.
     */
    public RepairOrderDTO rejectRepairOrder(int repairOrderId) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = findExistingRepairOrder(repairOrderId);
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

    private RepairOrder findExistingRepairOrder(int repairOrderId) throws RepairOrderNotFoundException {
        RepairOrder repairOrder = repairOrderRegistry.findRepairOrder(repairOrderId);
        if (repairOrder == null) {
            throw new RepairOrderNotFoundException(repairOrderId);
        }
        return repairOrder;
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
