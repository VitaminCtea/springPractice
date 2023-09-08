package spring.validatorTest.testValidatorGroups.groupSequenceAnnotationTest;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.AssertFalse;
import spring.validatorTest.testValidatorGroups.Car;
import spring.validatorTest.testValidatorGroups.CarChecks;

@GroupSequence({ RentalCar.class, RentalChecks.class, CarChecks.class })    // 必须使用当前类添加进验证组顺序中，否则没有一个默认的主类可以运行
public class RentalCar extends Car {
    @AssertFalse(message = "The car is currently rented out", groups = RentalChecks.class)
    private boolean rented;

    public RentalCar(String manufacturer, String licencePlate, int seatCount) {
        super(manufacturer, licencePlate, seatCount);
    }

    public boolean isRented() { return rented; }
    public void setRented(boolean rented) { this.rented = rented; }
}
