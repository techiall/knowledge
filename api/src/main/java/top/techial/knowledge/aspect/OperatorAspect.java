package top.techial.knowledge.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author techial
 */
@Aspect
@Component
@Log4j2
public class OperatorAspect {

    @Before("execution(public * top.techial.knowledge.controller..*(..))")
    public void before(JoinPoint joinPoint) {
        log.info(joinPoint);

        log.info(1);
    }

}
