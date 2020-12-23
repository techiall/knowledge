package cc.techial.knowledge.aop.authority;

import cc.techial.knowledge.service.UserService;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author techial
 */
@Aspect
public class FlushAuthorityAspect {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlushAuthorityAspect.class);

    private final UserService userService;

    public FlushAuthorityAspect(UserService userService) {
        this.userService = userService;
    }

    @After("@annotation(flushAuthority)")
    public void after(FlushAuthority flushAuthority) {
        userService.resetAuthority();
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("flush authority success.");
        }
    }

    @AfterThrowing(value = "@annotation(flushAuthority)", throwing = "throwable")
    public void afterThrowing(FlushAuthority flushAuthority, Throwable throwable) {
        if (LOGGER.isErrorEnabled()) {
            LOGGER.error("flush authority error.", throwable);
        }
    }
}
