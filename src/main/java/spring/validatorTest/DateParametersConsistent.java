package spring.validatorTest;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, CONSTRUCTOR, ANNOTATION_TYPE, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = DateParametersConsistentValidator.class)
public @interface DateParametersConsistent {

    // 默认的这种{ ... }格式是需要使用properties文件进行支持的，所以这里的默认值是一个表达式，而不仅仅是一个字符串，可以使用变量进行解析
    String message() default "{spring.validatorTest.DateParametersConsistent.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
