package se.kth.iv1350.repairebike.view;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import se.kth.iv1350.repairebike.model.RepairOrderObserver;
import se.kth.iv1350.repairebike.model.RepairOrderSnapshot;

/**
 * Writes repair order updates to a file.
 */
public class RepairOrderLogger implements RepairOrderObserver {
    private static final String LOG_FILE_NAME = "repair-order-log.txt";
    private final PrintWriter logStream;

    /**
     * Creates a repair order logger that writes to the default log file.
     */
    public RepairOrderLogger() {
        this(null);
    }

    /**
     * Creates a repair order logger that writes to the specified stream.
     *
     * @param logStream The stream to write to.
     */
    public RepairOrderLogger(PrintWriter logStream) {
        this.logStream = logStream;
    }

    /**
     * Writes the updated repair order to a file.
     *
     * @param repairOrder The updated repair order snapshot.
     */
    @Override
    public void repairOrderUpdated(RepairOrderSnapshot repairOrder) {
        if (logStream != null) {
            writeRepairOrderUpdate(repairOrder, logStream);
            logStream.flush();
            return;
        }

        try (
            FileWriter fileWriter = new FileWriter(LOG_FILE_NAME, true);
            PrintWriter fileLogStream = new PrintWriter(fileWriter)
        ) {
            writeRepairOrderUpdate(repairOrder, fileLogStream);
        } catch (IOException ioException) {
            System.err.println("Could not write repair order update to log: " + ioException.getMessage());
        }
    }

    private void writeRepairOrderUpdate(RepairOrderSnapshot repairOrder, PrintWriter stream) {
        stream.println("Repair order update written at " + LocalDateTime.now());
        stream.println(RepairOrderFormatter.formatRepairOrder(repairOrder));
        stream.println();
    }
}
