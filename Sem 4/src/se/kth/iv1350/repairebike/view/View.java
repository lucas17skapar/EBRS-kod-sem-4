package se.kth.iv1350.repairebike.view;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairebike.controller.Controller;
import se.kth.iv1350.repairebike.controller.CustomerNotFoundException;
import se.kth.iv1350.repairebike.controller.InvalidRepairOrderStateException;
import se.kth.iv1350.repairebike.controller.NoCurrentCustomerException;
import se.kth.iv1350.repairebike.controller.OperationFailedException;
import se.kth.iv1350.repairebike.controller.RepairOrderNotFoundException;
import se.kth.iv1350.repairebike.dto.BikeDTO;
import se.kth.iv1350.repairebike.dto.CustomerDTO;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;
import se.kth.iv1350.repairebike.dto.RepairTaskDTO;

/**
 * The console view with a hard-coded sample flow.
 */
public class View {
    private final Controller controller;
    private final ErrorMessageHandler errorMessageHandler;
    private final ErrorLogger errorLogger;

    /**
     * Creates a new view.
     *
     * @param controller The controller used by this view.
     */
    public View(Controller controller) {
        this(controller, new ErrorMessageHandler(), new ErrorLogger());
    }

    /**
     * Creates a new view with specified error handlers.
     *
     * @param controller The controller used by this view.
     * @param errorMessageHandler The handler used to show user-facing error messages.
     * @param errorLogger The logger used to write developer error reports.
     */
    public View(Controller controller, ErrorMessageHandler errorMessageHandler, ErrorLogger errorLogger) {
        this.controller = controller;
        this.errorMessageHandler = errorMessageHandler;
        this.errorLogger = errorLogger;
    }

    /**
     * Runs the seminar 4 sample flow.
     */
    public void sampleExecution() {
        try {
            CustomerDTO missingCustomer = findCustomer("0000000000");
            System.out.println("0a. Missing customer search result: " + formatCustomer(missingCustomer));

            CustomerDTO failedSearch = findCustomer("9999999999");
            System.out.println("0b. Database failure search result: " + formatCustomer(failedSearch));

            CustomerDTO customer = findCustomer("0701234567");
            System.out.println("1. Found customer: " + formatCustomer(customer));
            System.out.println("2. Found bike: " + formatBike(customer.getBike()));

            RepairOrderDTO createdOrder = controller.createRepairOrder("Battery drains quickly and motor cuts out.");
            System.out.println("3-4. Created repair order: " + formatRepairOrder(createdOrder));
            int repairOrderId = createdOrder.getOrderId();

            List<RepairTaskDTO> repairTasks = new ArrayList<>();
            repairTasks.add(new RepairTaskDTO("Replace battery connector", 900.0));
            repairTasks.add(new RepairTaskDTO("Update motor controller firmware", 650.0));
            System.out.println("5. Repair tasks: " + formatRepairTasks(repairTasks));

            LocalDate estimatedCompletionDate = createdOrder.getCreatedDate().plusDays(3);

            RepairOrderDTO updatedOrder = controller.addDiagnosticReport(
                repairOrderId,
                "Loose battery connector found and outdated firmware detected.",
                repairTasks,
                estimatedCompletionDate
            );
            System.out.println("6-7. Updated repair order: " + formatRepairOrder(updatedOrder));

            System.out.println("7. Total cost: " + formatAmount(updatedOrder.getTotalCost()));

            RepairOrderDTO orderForApproval = controller.prepareRepairOrderForApproval(repairOrderId);
            System.out.println("8. Repair order for approval: " + formatRepairOrder(orderForApproval));

            System.out.println("9. Printer output:");
            RepairOrderDTO acceptedOrder = controller.acceptRepairOrder(repairOrderId);
            if ("ACCEPTED".equals(acceptedOrder.getState())) {
                controller.printRepairOrder(formatRepairOrderReceipt(acceptedOrder));
            }

            System.out.println("10. Accepted repair order: " + formatRepairOrder(acceptedOrder));
        } catch (NoCurrentCustomerException | RepairOrderNotFoundException | InvalidRepairOrderStateException exc) {
            errorMessageHandler.showErrorMessage("The repair order operation could not be completed.");
            errorLogger.logException(exc);
        }
    }

    CustomerDTO findCustomer(String phoneNumber) {
        try {
            return controller.findCustomer(phoneNumber);
        } catch (CustomerNotFoundException exc) {
            errorMessageHandler.showErrorMessage(
                "No customer was found with phone number " + exc.getSearchedPhoneNumber() + "."
            );
        } catch (OperationFailedException exc) {
            errorMessageHandler.showErrorMessage(
                "The customer search could not be completed. Please try again later."
            );
            errorLogger.logException(exc);
        }
        return null;
    }

    private String formatRepairOrder(RepairOrderDTO repairOrder) {
        return RepairOrderFormatter.formatRepairOrder(repairOrder);
    }

    private String formatRepairOrderReceipt(RepairOrderDTO repairOrder) {
        return RepairOrderFormatter.formatRepairOrderReceipt(repairOrder);
    }

    private String formatCustomer(CustomerDTO customer) {
        return RepairOrderFormatter.formatCustomer(customer);
    }

    private String formatBike(BikeDTO bike) {
        return RepairOrderFormatter.formatBike(bike);
    }

    private String formatRepairTasks(List<RepairTaskDTO> repairTasks) {
        return RepairOrderFormatter.formatRepairTasks(repairTasks);
    }

    private String formatAmount(BigDecimal amount) {
        return RepairOrderFormatter.formatAmount(amount);
    }
}
