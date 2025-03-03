package web.forum.imageservice.aspect;

import jakarta.servlet.http.*;
import lombok.extern.slf4j.*;
import org.aspectj.lang.*;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.web.context.request.*;

import java.util.*;

@Component
@Aspect
@Slf4j
public class AspectLogging {
    @Pointcut("execution(public * web.forum.imageservice.controller.*.*(..))")
    public void controllerRestLog(){}
    @Pointcut("execution(public * web.forum.imageservice.service.impl.*.*(..))")
    public void serviceLog(){}

    @Before("controllerRestLog()")
    public void doBeforeController(JoinPoint jp){
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request;

        request = attributes.getRequest();

        log.info("NEW REQUEST: IP: {}, URL: {}, HTTP_METHOD: {}, CONTROLLER_METHOD: {}.{}",
                request.getRemoteAddr(),
                request.getRequestURL().toString(),
                request.getMethod(),
                jp.getSignature().getDeclaringTypeName(),
                jp.getSignature().getName());

    }

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint jp){
        String className = jp.getSignature().getDeclaringTypeName();
        String methodName = jp.getSignature().getName();

        Object[] args = jp.getArgs();
        String argsString = args.length > 0 ? Arrays.toString(args) : "METHOD HAS NO ARGUMENTS";

        log.info("RUN SERVICE: SERVICE_METHOD: {}.{}. METHOD ARGUMENTS: [{}]",
                className, methodName, argsString);
    }

    @AfterReturning(returning = "returnObject", pointcut = "serviceLog()")
    public void doAfterReturningService(Object returnObject){
        log.info("Return value from service: {}", returnObject);
    }

    @AfterReturning(returning = "returnObject", pointcut = "controllerRestLog()")
    public void doAfterReturning(Object returnObject){
        log.info("Return value: {}", returnObject);
    }

    @After("controllerRestLog()")
    public void doAfter(JoinPoint jp){
        log.info("Controller Method executed successfully: {}.{}",
                jp.getSignature().getDeclaringTypeName(),
                jp.getSignature().getName());
    }

    @Around("controllerRestLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        log.info("Execution method: {}.{}. Execution time: {}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                executionTime);

        return proceed;

    }


    @AfterThrowing(throwing = "ex", pointcut = "controllerRestLog()")
    public void throwsException(JoinPoint jp, Exception ex){
        String methodName = jp.getSignature().getName();
        String className = jp.getTarget().getClass().getSimpleName();

        log.error("Exception in {}.{} with arguments {}. Exception message: {}",
                className, methodName, Arrays.toString(jp.getArgs()), ex.getMessage());
    }
}
