package se.kth.iv1350.repairebike.model;

/**
 * Represents an electric bike.
 */
public class Bike {
    private final String brand;
    private final String model;
    private final String serialNumber;

    /**
     * Creates a bike.
     *
     * @param brand The bike brand.
     * @param model The bike model.
     * @param serialNumber The bike serial number.
     */
    public Bike(String brand, String model, String serialNumber) {
        this.brand = brand;
        this.model = model;
        this.serialNumber = serialNumber;
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

}
