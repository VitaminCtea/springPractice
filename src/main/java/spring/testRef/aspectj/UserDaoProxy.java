package spring.testRef.aspectj;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class UserDaoProxy {
    // 相当于class proxy implements Animal, Person {}
    @DeclareParents(value = "spring.testRef.aspectj.Person+", defaultImpl = FemaleAnimal.class)
    public Animal mixin;

    private boolean usedJoinPoint;
    // Pointcut(切入点相当于需要被增强的方法，这里需要注意的是，需要被增强的是同一个类时候，aop不会生效，不知道为什么)
    // 增强UserDao类所有以show方法名称开头的方法
    // 任何作为增强方法的方法都接收一个JoinPoint为第一个参数，如：@Before("pointDemo()")、@After("pointDemo()")，这样的称为建议
    // JoinPoint包含切入点的所有信息
    @Pointcut("execution(* spring.testRef.aspectj.UserDao.show*(..))")
    private void pointDemo() {}

    @Before("pointDemo()")
    public void before(JoinPoint joinPoint) {
        System.out.println("before running...");
        if (!usedJoinPoint) {
            usedJoinPoint = true;
            System.out.println("joinPoint: " + joinPoint);
            System.out.println("Signature: " + joinPoint.getSignature());
            System.out.println("Args: " + Arrays.toString(joinPoint.getArgs()));
            System.out.println("Target: " + joinPoint.getTarget());
            System.out.println("This: " + joinPoint.getThis());
        }
    }

    @After("pointDemo()")
    public void after(JoinPoint joinPoint) { System.out.println("after running..."); }

    @AfterReturning(pointcut = "pointDemo()", returning = "retVal")
    public void afterReturning(JoinPoint joinPoint, String retVal) {
        System.out.println("afterReturning running, The returned value is: " + retVal);
    }

    @AfterThrowing(pointcut = "pointDemo()", throwing = "ex")
    public void afterThrowing(JoinPoint joinPoint, Throwable ex) { System.out.println("afterThrowing running..."); }
}
