package spring.validatorTest;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.constraintvalidation.SupportedValidationTarget;
import jakarta.validation.constraintvalidation.ValidationTarget;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import java.time.LocalDate;

/*
* 等价于在每个DateParametersConsistentValidator注解上使用validationAppliesTo(即：注解验证的目标)
* 默认的方法验证规则为：
* 1. 如果方法有参数但没有返回值；
* 2. 如果没有参数但有返回值；
* 3. 既不是方法也不是构造函数，而是字段、参数等(约束适用于带注释的元素)
* 如果没有在类中统一设置一个应用验证目标的话，则需要在注解中声明ConstraintTarget validationAppliesTo() default ConstraintTarget.IMPLICIT;
* ConstraintTarget.RETURN_VALUE(应用到返回值)  ConstraintTarget.PARAMETERS(应用到形参参数)  ConstraintTarget.IMPLICIT(默认自动推断)
* */
@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DateParametersConsistentValidator implements ConstraintValidator<DateParametersConsistent, Object[]> {
    @Override
    public void initialize(DateParametersConsistent constraintAnnotation) {
        System.out.println("DateParametersConsistentValidator执行了...");
    }

    @Override
    public boolean isValid(Object[] value, ConstraintValidatorContext constraintValidatorContext) {
        if (value.length != 3) throw new IllegalArgumentException("Unexpected method signature");
        if (value[1] == null || value[2] == null) return true;
        String country = constraintValidatorContext.unwrap(HibernateConstraintValidatorContext.class).getConstraintValidatorPayload(String.class);
        if (country != null) {
            // 当测试验证ConstraintValidatorPayload时，由于注解的值是固定的，此时如果想更改错误信息的话，可以以这种方式对原错误信息进行覆盖
            constraintValidatorContext.disableDefaultConstraintViolation();
            constraintValidatorContext.buildConstraintViolationWithTemplate("Error validating payload").addConstraintViolation();
            printCountry(country);
        }
        return ((LocalDate) value[1]).isBefore((LocalDate) value[2]);
    }

    private void printCountry(String country) { System.out.printf("ConstraintValidatorPayload is %s...%n", country); }
}
