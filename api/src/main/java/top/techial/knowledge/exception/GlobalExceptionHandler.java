package top.techial.knowledge.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.MediaTypeNotSupportedStatusException;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;

/**
 * @author caogaoyang
 */

@Log4j2
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

    @ExceptionHandler({
        IllegalArgumentException.class,
        UserNotFoundNodeException.class,
        NodeNotFoundException.class,
        UserException.class,
        MethodArgumentNotValidException.class,
        MissingServletRequestParameterException.class,
        JsonProcessingException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Object illegalArgumentException(Throwable throwable) {
        if (log.isWarnEnabled()) {
            log.warn(throwable.getMessage());
        }
        return new ResultBean<>(ResultCode.CHECK_FAIL);
    }

    @ExceptionHandler(MediaTypeNotSupportedStatusException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public Object mediaTypeNotSupportedStatusException(Throwable throwable) {
        if (log.isWarnEnabled()) {
            log.warn(throwable.getMessage());
        }
        return new ResultBean<>(ResultCode.NO_PERMISSION);
    }


}
