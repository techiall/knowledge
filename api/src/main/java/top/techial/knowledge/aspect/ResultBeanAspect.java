package top.techial.knowledge.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import top.techial.beans.ResultBean;
import top.techial.spring.data.PageDTO;

/**
 * convent {@link Page} to {@link PageDTO}
 *
 * @author techial
 */
@Aspect
@Component
@Log4j2
public class ResultBeanAspect {

    private static ResultBean<PageDTO> convent(Page<?> page) {
        return new ResultBean<>(new PageDTO()
            .setContent(page.getContent())
            .setTotalElements(page.getTotalElements())
            .setPageSize(page.getPageable().getPageSize())
            .setPageNum(page.getPageable().getPageNumber())
            .setTotalPages(page.getTotalPages()));
    }

    @Around(value = "execution(public top.techial.beans.ResultBean *(..))")
    public Object invoke(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        ResultBean<?> result = (ResultBean<?>) proceedingJoinPoint.proceed();
        Object data = result.getData();
        if (data instanceof Page) {
            Page<?> page = (Page<?>) data;
            return ResultBeanAspect.convent(page);
        }
        return result;
    }

    @AfterThrowing(value = "execution(public top.techial.beans.ResultBean *(..))", throwing = "throwable")
    public void afterThrowing(Throwable throwable) {
        if (log.isWarnEnabled()) {
            log.warn(throwable.getMessage());
        }
    }
}
