package spring.validatorTest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.executable.ExecutableValidator;
import jakarta.validation.groups.Default;
import jakarta.validation.metadata.BeanDescriptor;
import jakarta.validation.metadata.MethodType;
import org.hibernate.validator.HibernateValidatorFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import spring.testRef.Outer;
import spring.validatorTest.testValidatorGroups.CarChecks;
import spring.validatorTest.testValidatorGroups.Driver;
import spring.validatorTest.testValidatorGroups.DriverChecks;
import spring.validatorTest.testValidatorGroups.groupSequenceAnnotationTest.RentalCar;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;

import static org.junit.Assert.*;

public class CarTest {

    private static Validator validator;
    private static ValidatorFactory factory;

    @BeforeClass
    public static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void manufacturerIsNull() {
        createValidatorInstanceCheckSizeAndOther(
                new Car( null, "DD-AB-123", 4 ),
                "must not be null");
    }

    @Test
    public void licensePlateTooShort() {
        createValidatorInstanceCheckSizeAndOther(
                new Car( "Morris", "D", 4 ),
                "size must be between 2 and 14");
    }

    @Test
    public void seatCountTooLow() {
        createValidatorInstanceCheckSizeAndOther(
                new Car( "Morris", "DD-AB-123", 1 ),
                "must be greater than or equal to 2");
    }

    @Test
    public void carIsValid() {
        Set<ConstraintViolation<Car>> constraintViolations = getConstraintViolations(new Car( "Morris", "DD-AB-123", 2 ));
        assertEquals(
                0,
                constraintViolations.size());
    }

    @Test
    public void attributeLevelConstraint() {
        printConstraintViolationInterfaceMethodReturnValue(getConstraintViolation(new Car( null, "DD-AB-123", 2 )));
        assertEquals(
                "manufacturer is null",
                getErrorMessage(new Car( null, "DD-AB-123", 2 )));
    }

    @Test
    public void validationMethodConstraints() {
        testValidationMethodConstraintsMethodInOuterClass(factory.getValidator().forExecutables(), "我是你爸");
    }

    @Test
    public void expectedErrorMessage() {
        Car car = new Car(null, "A", 1, 400.123456, BigDecimal.valueOf(200000));
        String message = validator.validateProperty(car, "manufacturer").iterator().next().getMessage();
        assertEquals("不能为null", message);

        message = validator.validateProperty(car, "licensePlate").iterator().next().getMessage();
        assertEquals("The license plate '$ABC' must be between 2 and 14 characters long", message);

        message = validator.validateProperty(car, "seatCount").iterator().next().getMessage();
        assertEquals( "There must be at least 2 seats", message );

        message = validator.validateProperty(car, "topSpeed").iterator().next().getMessage();
        assertEquals( "The top speed 400.12 is higher than 350", message );

        message = validator.validateProperty( car, "price" ).iterator().next().getMessage();
        assertEquals( "Price must not be higher than $100000", message );
    }

    @Test
    public void useValidationGroups() {
        spring.validatorTest.testValidatorGroups.Car car = new spring.validatorTest.testValidatorGroups.Car("Morris", "DD-AB-123", 2);
        Set<ConstraintViolation<spring.validatorTest.testValidatorGroups.Car>> constraintViolations = getConstraintViolations(car);
        assertEquals(0, getConstraintViolationsSize(constraintViolations)); // 默认验证默认组(Default.class)

        constraintViolations = getConstraintViolations(car, CarChecks.class);   // 验证CarChecks组，passedVehicleInspection默认为false
        assertEquals(1, getConstraintViolationsSize(constraintViolations));
        assertEquals("The car has to pass the vehicle inspection first", getErrorMessage(constraintViolations));

        car.setPassedVehicleInspection(true);   // 当设置为true时，验证通过
        assertEquals(0, getConstraintViolationsSize(getConstraintViolations(car, CarChecks.class)));

        Driver john = new Driver("John Doe", 18);
        car.setDriver(john);    // 设置驾驶员
        constraintViolations = getConstraintViolations(car, DriverChecks.class);    // 验证DriverChecks组
        assertEquals(1, getConstraintViolationsSize(constraintViolations)); // 因为hasDrivingLicense为false，所以验证不通过
        assertEquals("You first have to pass the driving test", getErrorMessage(constraintViolations));

        john.passedDrivingTest(true);   // 设置true时，验证通过
        assertEquals(0, getConstraintViolationsSize(getConstraintViolations(car, DriverChecks.class)));
        assertEquals(0, getConstraintViolationsSize(getConstraintViolations(car, Default.class, CarChecks.class, DriverChecks.class)));
    }

    @Test
    public void testGroupSequenceAnnotation() {
        RentalCar rentalCar = new RentalCar("Morris", "DD-AB-123", 2);
        rentalCar.setPassedVehicleInspection(true); // 首先查看车辆是否合格(即：车检)
        rentalCar.setRented(true);  // 设置租赁状态

        Set<ConstraintViolation<RentalCar>> constraintViolations = getConstraintViolations(rentalCar);
        assertEquals(1, getConstraintViolationsSize(constraintViolations)); // 因为只有rented为假才通过测试，上面设置了为true
        assertEquals("The car is currently rented out", getErrorMessage(constraintViolations));

        rentalCar.setRented(false); // 恢复原状态，测试通过
        assertEquals(0, getConstraintViolationsSize(getConstraintViolations(rentalCar)));
    }

    @Test
    public void testConvertGroup() {
        // 当driver为null时，则不进行验证，默认为Default组，当使用@ConvertGroup注解时，这个对象(Driver)内的分组就成了Car组的一部分，如果driver不为null，则和Default分组一起验证
        // 从而不必在显示的传入要验证的分组，需要注意的是@ConvertGroup注解只能使用在带有@Valid注解的字段
        spring.validatorTest.testValidatorGroups.Car car = new spring.validatorTest.testValidatorGroups.Car("VW", "USD-123", 4);
        car.setPassedVehicleInspection(true);
        Set<ConstraintViolation<spring.validatorTest.testValidatorGroups.Car>> constraintViolations = getConstraintViolations(car);
        assertEquals(0, getConstraintViolationsSize(constraintViolations));

        Driver john = new Driver("John Doe", 18);
        car.setDriver(john);
        constraintViolations = getConstraintViolations(car);
        assertEquals(1, getConstraintViolationsSize(constraintViolations));
        assertEquals(
                "The driver constraint should also be validated as part of the default group",
                constraintViolations.iterator().next().getMessage(),
                "You first have to pass the driving test"
        );
    }

//    @Test
//    public void testConstraintValidatorPayload() {
//        /*
//            ValidatorFactory validatorFactory =
//                    Validation.byProvider(HibernateValidator.class)
//                            .configure()
//                            .constraintValidatorPayload("US")
//                            .buildValidatorFactory();
//            Validator validator = validatorFactory.getValidator();
//            或使用下面这种方式进行传入Payload，两者等价！
//         */
//        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().buildValidatorFactory()/* 获取ValidatorFactory */) {
//            // 把工厂转换为HibernateValidatorFactory(强转，父转子)
//            HibernateValidatorFactory hibernateValidatorFactory = validatorFactory.unwrap(HibernateValidatorFactory.class);
//            ExecutableValidator executableValidator =
//                    hibernateValidatorFactory.usingContext()
//                            .constraintValidatorPayload("US")
//                            .getValidator()
//                            .forExecutables();
//            testValidationMethodConstraintsMethodInOuterClass(executableValidator, "Error validating payload");
//        }
//    }

    private void testValidationMethodConstraintsMethodInOuterClass(ExecutableValidator executableValidator, String errorMessage) {
        Outer outer = new Outer();
        final String TEST_METHOD_NAME = "validationMethodConstraints";
        checkSizeAndOther(errorMessage, outer, instance -> {
            try {
                Class<?> cls = instance.getClass();
                Method method;
                Set<ConstraintViolation<Outer>> constraintViolationSet = executableValidator.validateParameters(
                        outer,
                        /* 此处仅仅是不想显示传入所需所有形参的class */
                        method = cls.getDeclaredMethod(
                                TEST_METHOD_NAME,
                                Arrays.stream(cls.getDeclaredMethods())
                                        .filter(x -> x.getName().equals(TEST_METHOD_NAME))
                                        .findFirst()
                                        .orElseThrow(() -> new NullPointerException(String.format("%s method not found.", TEST_METHOD_NAME)))
                                        .getParameterTypes()),
                        new Object[] {
                                1,
                                LocalDate.now(),
                                LocalDate.of(1990, Calendar.DECEMBER, 22)
                        });

                Iterator<ConstraintViolation<Outer>> iterator = constraintViolationSetIterator(constraintViolationSet);
                Annotation[] annotations = method.getDeclaredAnnotations();
                ConstraintViolation<Outer> violation = iterator.next();
                final int LENGTH = annotations.length;
                for (int i = 0; i < LENGTH && iterator.hasNext(); i++, violation = iterator.next())
                    assertEquals(annotations[i].getClass(), violation.getConstraintDescriptor().getAnnotation().annotationType());

                return constraintViolationSet;
            } catch (NoSuchMethodException e) {
                throw (RuntimeException) e.fillInStackTrace();
            }
        });

        printBeanConstraintMetaData(spring.validatorTest.testValidatorGroups.Car.class, validator);
    }

    private void printBeanConstraintMetaData(Class<?> clazz, Validator validator) {
        BeanDescriptor descriptor = validator.getConstraintsForClass(clazz);

        assertTrue(descriptor.isBeanConstrained()); // 验证这个类中使用了注解(类中或类外)

        assertEquals(0, descriptor.getConstraintDescriptors().size());  // 类层次没有注解约束
        assertEquals(5, descriptor.getConstrainedProperties().size());  // 使用注解约束的字段个数
        // manufacturer字段使用了多少个注解约束
        assertEquals(1, descriptor.getConstraintsForProperty("manufacturer").getConstraintDescriptors().size());
        assertEquals(1, descriptor.getConstrainedConstructors().size());    // 有几个构造函数使用了注解约束
        assertEquals(1, descriptor.getConstrainedMethods(MethodType.NON_GETTER).size());    // 有几个不是getter方法(以get或is开头的)使用注解约束的
        assertEquals(2, descriptor.getConstrainedMethods(MethodType.NON_GETTER, MethodType.GETTER).size()); // 同上，不是getter和是getter

        assertNull(descriptor.getConstraintsForProperty("modelName"));  // 验证modelName字段没有使用注解约束
        assertNull(descriptor.getConstraintsForMethod("setManufacturer", String.class));    // 验证setManufacturer方法参数没有使用注解约束

        assertNotNull(descriptor.getConstraintsForProperty("licensePlate"));    // 验证licensePlate字段使用了注解约束
        assertNotNull(descriptor.getConstraintsForProperty("driver"));  // 验证driver字段使用了注解约束
        assertNotNull(descriptor.getConstraintsForMethod("driveAway", int.class));  // 验证driveAway方法中的参数使用了注解约束
        assertNotNull(descriptor.getConstraintsForMethod("getManufacturer"));   // 验证getManufacturer方法中的参数使用了注解约束
        // 验证有一个构造方法中的参数使用了注解约束(注意：这里不能使用Integer.class，因为Integer.class != int.class)
        assertNotNull(descriptor.getConstraintsForConstructor(String.class, String.class, int.class, Driver.class));
    }

    private <T> void createValidatorInstanceCheckSizeAndOther(T obj, String errorMessage) {
        checkSizeAndOther(errorMessage, obj, this::getConstraintViolations);
    }

    private <T> void checkSizeAndOther(String errorMessage, T obj, Function<T, Set<ConstraintViolation<T>>> createdConstraintViolation) {
        Set<ConstraintViolation<T>> constraintViolations = getConstraintViolations(obj, createdConstraintViolation);
        assertEquals( 1, getConstraintViolationsSize(constraintViolations));
        assertEquals(errorMessage, getErrorMessage(constraintViolations));
    }

    private <T> String getErrorMessage(T obj) { return getConstraintViolation(obj).getMessage(); }

    private <T> String getErrorMessage(Set<ConstraintViolation<T>> constraintViolationSet) {
        return getConstraintViolation(constraintViolationSet).getMessage();
    }

    private <T> Set<ConstraintViolation<T>> getConstraintViolations(T obj) { return validator.validate(obj); }
    private <T> Set<ConstraintViolation<T>> getConstraintViolations(T obj, Class<?>... groups) { return validator.validate(obj, groups); }
    private <T> Set<ConstraintViolation<T>> getConstraintViolations(T obj, Function<T, Set<ConstraintViolation<T>>> createdConstraintViolation) {
        return createdConstraintViolation.apply(obj);
    }

    private <T> Iterator<ConstraintViolation<T>> constraintViolationSetIterator(Set<ConstraintViolation<T>> constraintViolationSet) {
        return constraintViolationSet.iterator();
    }

    private <T> ConstraintViolation<T> getConstraintViolation(T obj) { return constraintViolationSetIterator(getConstraintViolations(obj)).next(); }
    private <T> ConstraintViolation<T> getConstraintViolation(Set<ConstraintViolation<T>> constraintViolationSet) {
        return constraintViolationSetIterator(constraintViolationSet).next();
    }

    private <T> void printConstraintViolationInterfaceMethodReturnValue(ConstraintViolation<T> constraintViolation) {
        Set<String> stringSet = Arrays.stream(Object.class.getMethods()).collect(HashSet::new, (r, c) -> r.add(c.getName()), (r1, r2) -> {});
        for (Method method: constraintViolation.getClass().getDeclaredMethods()) {
            String methodName = method.getName();
            if (stringSet.contains(methodName) || method.getParameterCount() > 0 || Modifier.isPrivate(method.getModifiers())) continue;
            try {
                System.out.println(methodName + ": " + method.invoke(constraintViolation) + ", type: " + method.getReturnType().getName());
            } catch (InvocationTargetException | IllegalAccessException e) { e.printStackTrace(); }
        }
    }

    private <T> int getConstraintViolationsSize(Set<ConstraintViolation<T>> constraintViolationSet) { return constraintViolationSet.size(); }
}
