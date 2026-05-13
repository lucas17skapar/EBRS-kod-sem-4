package se.kth.iv1350.repairebike.dto;

/**
 * Contains bike data that may be shown by the view.
 */
public class BikeDTO {
    private final String brand;
    private final String model;
    private final String serialNumber;

    /**
     * Creates a new bike DTO.
     *
     * @param brand The bike brand.
     * @param model The bike model.
     * @param serialNumber The bike serial number.
     */
    public BikeDTO(String brand, String model, String serialNumber) {
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
     * Gets the bike serial number.
     *
     * @return The bike serial number.
     */
    public String getSerialNumber() {
        return serialNumber;
    }
}
