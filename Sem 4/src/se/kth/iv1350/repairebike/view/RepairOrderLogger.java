package se.kth.iv1350.repairebike.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import se.kth.iv1350.repairebike.controller.RepairOrderObserver;
import se.kth.iv1350.repairebike.dto.RepairOrderDTO;

/**
 * Writes repair order updates to a file.
 */
public class RepairOrderLogger implements RepairOrderObserver {
    private static final String LOG_FILE_NAME = "repair-order-log.txt";

    /**
     * Writes the updated repair order to a file.
     *
     * @param repairOrder The updated repair order.
     */
    @Override
    public void repairOrderUpdated(RepairOrderDTO repairOrder) {
        try (
            FileWriter fileWriter = new FileWriter(LOG_FILE_NAME, true);
            PrintWriter logStream = new PrintWriter(fileWriter)
        ) {
            logStream.println("Repair order update written at " + LocalDateTime.now());
            logStream.println(repairOrder);
            logStream.println();
        } catch (IOException ioException) {
            System.err.println("Could not write repair order update to log: " + ioException.getMessage());
        }
    }
}
