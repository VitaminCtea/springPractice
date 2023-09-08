package spring.validatorTest.testValidatorGroups;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Min;

public class Driver extends Person {
    @Min(value = 18, message = "You have to be 18 to drive a car.", groups = DriverChecks.class)
    private final int age;

    @AssertTrue(message = "You first have to pass the driving test", groups = DriverChecks.class)
    private boolean hasDrivingLicense;

    public Driver(String name, int age) {
        super(name);
        this.age = age;
    }

    public void passedDrivingTest(boolean b) { hasDrivingLicense = b; }
    public int getAge() { return age; }
}
