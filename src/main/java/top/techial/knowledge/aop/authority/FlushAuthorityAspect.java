package top.techial.knowledge.aop.authority;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import top.techial.knowledge.service.UserService;

/**
 * @author techial
 */
@Aspect
@Component
@Log4j2
public class FlushAuthorityAspect {

    private final UserService userService;

    public FlushAuthorityAspect(UserService userService) {
        this.userService = userService;
    }

    @After("@annotation(flushAuthority)")
    public void after(FlushAuthority flushAuthority) {
        userService.resetAuthority();
        if (log.isDebugEnabled()) {
            log.debug("flush authority success.");
        }
    }


    @AfterThrowing(value = "@annotation(flushAuthority)", throwing = "throwable")
    public void afterThrowing(FlushAuthority flushAuthority, Throwable throwable) {
        if (log.isErrorEnabled()) {
            log.error("flush authority error.", throwable);
        }
    }
}
