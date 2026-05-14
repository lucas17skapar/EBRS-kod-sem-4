package se.kth.iv1350.repairebike.model;

import java.time.LocalDate;

/**
 * Represents an electric bike.
 */
public class Bike {
    private final String brand;
    private final String model;
    private final String serialNumber;
    private final LocalDate warrantyEndDate;

    /**
     * Creates a bike.
     *
     * @param brand The bike brand.
     * @param model The bike model.
     * @param serialNumber The bike serial number.
     */
    public Bike(String brand, String model, String serialNumber) {
        this(brand, model, serialNumber, LocalDate.MIN);
    }

    /**
     * Creates a bike.
     *
     * @param brand The bike brand.
     * @param model The bike model.
     * @param serialNumber The bike serial number.
     * @param warrantyEndDate The last day covered by warranty.
     */
    public Bike(String brand, String model, String serialNumber, LocalDate warrantyEndDate) {
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
        this.warrantyEndDate = warrantyEndDate;
    }

    /**
     * Gets the bike brand.
     *
     * @return The bike brand.
     */
    public String getBrand() {
        return brand;
    }

    /**
     * Gets the bike model.
     *
     * @return The bike model.
     */
    public String getModel() {
        return model;
    }

    /**
     * Gets the serial number.
     *
     * @return The serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * Gets the last day covered by warranty.
     *
     * @return The warranty end date.
     */
    public LocalDate getWarrantyEndDate() {
        return warrantyEndDate;
    }

    /**
     * Checks if this bike is covered by warranty on the specified date.
     *
     * @param date The date to check.
     * @return {@code true} if the bike is covered by warranty, otherwise {@code false}.
     */
    public boolean isUnderWarranty(LocalDate date) {
        return !warrantyEndDate.isBefore(date);
    }

}
