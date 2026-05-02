package se.kth.iv1350.repairebike.model;

/**
 * Represents a diagnostic report.
 */
public class DiagnosticReport {
    private final String reportText;

    /**
     * Creates a diagnostic report.
     *
     * @param reportText The report text.
     */
    public DiagnosticReport(String reportText) {
        this.reportText = reportText;
    }

    /**
     * Gets the report text.
     *
     * @return The report text.
     */
    public String getReportText() {
        return reportText;
    }

    /**
     * Returns a string representation of the diagnostic report.
     *
     * @return A diagnostic report string.
     */
    @Override
    public String toString() {
        return "DiagnosticReport{reportText='" + reportText + "'}";
    }
}
