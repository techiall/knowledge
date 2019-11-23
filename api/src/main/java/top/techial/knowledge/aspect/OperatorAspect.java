package top.techial.knowledge.aspect;

import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.Operator;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.dto.NodeInfoDTO;
import top.techial.knowledge.service.RecordService;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author techial
 */
@Aspect
@Component
@Log4j2
@Order(100)
public class OperatorAspect {
    private final RecordService recordService;

    public OperatorAspect(RecordService recordService) {
        this.recordService = recordService;
    }

    private static Map<String, Object> getParam(ProceedingJoinPoint proceedingJoinPoint) {
        Map<String, Object> map = new HashMap<>();
        String[] paramNames = ((CodeSignature) proceedingJoinPoint.getSignature()).getParameterNames();
        Object[] paramValues = proceedingJoinPoint.getArgs();

        for (int i = 0; i < paramNames.length; i++) {
            map.put(paramNames[i], paramValues[i]);
        }

        return map;
    }

    @Around("execution(public * top.techial.knowledge.controller.Knowledge*.*(..))")
    public Object before(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        Object result = joinPoint.proceed();

        if (method.isAnnotationPresent(PutMapping.class)) {
            record(result, getParam(joinPoint), Operator.UPDATE);
        } else if (method.isAnnotationPresent(PostMapping.class)) {
            record(result, getParam(joinPoint), Operator.ADD);
        }

        return result;
    }


    private void record(Object result, Object input, Operator operator) {
        if (!(result instanceof ResultBean)) {
            return;
        }
        ResultBean node = (ResultBean) result;
        if (!(node.getData() instanceof NodeInfoDTO)) {
            return;
        }
        NodeInfoDTO data = (NodeInfoDTO) node.getData();
        recordService.save(new Record()
            .setUserId(1)
            .setNodeId(data.getId())
            .setOperator(operator));
    }

}
