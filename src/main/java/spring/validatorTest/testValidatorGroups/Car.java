package spring.validatorTest.testValidatorGroups;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.ConvertGroup;
import jakarta.validation.groups.Default;

public class Car {
    @NotNull
    private String manufacturer;

    @NotNull
    @Size(min = 2, max = 14)
    private String licensePlate;

    @Min(2)
    private int seatCount;

    @AssertTrue(message = "The car has to pass the vehicle inspection first", groups = CarChecks.class)
    private boolean passedVehicleInspection;

    @Valid
    @ConvertGroup(from = Default.class, to = DriverChecks.class)
    private Driver driver;

    private String modelName;

    public Car(String manufacturer, String licencePlate, int seatCount) {
        this(manufacturer, licencePlate, seatCount, null);
    }

    public Car(@NotBlank String manufacturer, @NotBlank String licensePlate, @NotNull @Positive int seatCount, @NotNull Driver driver) {
        this.manufacturer = manufacturer;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.driver = driver;
    }

    public boolean isPassedVehicleInspection() { return passedVehicleInspection; }
    public void setPassedVehicleInspection(boolean passedVehicleInspection) { this.passedVehicleInspection = passedVehicleInspection; }

    public Driver getDriver() { return driver; }
    public void setDriver(Driver driver) { this.driver = driver; }

    @NotNull
    public String getManufacturer() { return manufacturer; }
    public void setManufacturer(String manufacturer) { this.manufacturer = manufacturer; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public int getSeatCount() { return seatCount; }
    public void setSeatCount(int seatCount) { this.seatCount = seatCount; }

    public void driveAway(@NotNull int away) {}
}
