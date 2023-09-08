package spring.validatorTest;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

// 建议在一个类中坚持使用字段"或"属性批注。不建议对字段和随附的 getter 方法进行批注，因为这会导致字段验证两次。
public class Car {

    @NotNull
    private String manufacturer;

    @NotNull
    @Size(
            min = 2,
            max = 14,
            message = "The license plate '${validatedValue}' must be between {min} and {max} characters long"
    )
    private String licensePlate;

    @Min(value = 2, message = "There must be at least {value} seat${value > 1 ? 's' : ''}")
    private int seatCount;

    @DecimalMax(value = "350", message = "The top speed ${formatter.format('%1$.2f', validatedValue)} is higher than {value}")
    private double topSpeed;

    @DecimalMax(value = "100000", message = "Price must not be higher than ${value}")
    private BigDecimal price;

    public Car(String manufacturer, String licencePlate, int seatCount) {
        this.manufacturer = manufacturer;
        this.licensePlate = licencePlate;
        this.seatCount = seatCount;
    }

    public Car(String manufacturer, String licensePlate, int seatCount, double topSpeed, BigDecimal price) {
        this.manufacturer = manufacturer;
        this.licensePlate = licensePlate;
        this.seatCount = seatCount;
        this.topSpeed = topSpeed;
        this.price = price;
    }

    @NotNull
    public String getManufacturer() {
        return manufacturer;
    }

    @NotNull
    public String getLicensePlate() {
        return licensePlate;
    }

    public int getSeatCount() {
        return seatCount;
    }

    @Override
    public String toString() {
        return "Car{" +
                "manufacturer='" + manufacturer + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", seatCount=" + seatCount +
                '}';
    }

    //getters and setters ...
}