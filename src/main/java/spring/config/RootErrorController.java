package spring.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import spring.error.CustomFileAlreadyExistsException;
import spring.error.TestException;

// 使用 @ControllerAdvice，为所有的控制器处理异常，例如下面的CustomFileAlreadyExistsException异常
// 当所有Controller抛出CustomFileAlreadyExistsException异常时，都会在这里进行统一处理，而不必为某些Controller方法所抛出的同一个异常而重复书写@ExceptionHandler注解
@ControllerAdvice
public class RootErrorController {
    @ExceptionHandler(CustomFileAlreadyExistsException.class)
    public String handleCustomFileAlreadyExistsException() { return "error/fileAlready"; }

    @ExceptionHandler(TestException.class)
    public String handleTestException() { return "error/testError"; }
}
