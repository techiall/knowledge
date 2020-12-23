package cc.techial.knowledge.aop.authority;

import java.lang.annotation.*;

/**
 * @author techial
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface FlushAuthority {
}
