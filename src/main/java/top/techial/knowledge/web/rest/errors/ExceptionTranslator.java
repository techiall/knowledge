package top.techial.knowledge.web.rest.errors;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.MediaTypeNotSupportedStatusException;
import top.techial.knowledge.beans.ResultBean;

@ControllerAdvice
public class ExceptionTranslator {

    private static ResponseEntity<ResultBean<Object>> handleException(ClientErrorException exception) {
        return ResponseEntity.status(exception.getCode())
                .body(exception.getResultBean());
    }

    private static ResponseEntity<ResultBean<String>> handleException(ResultBean<String> resultBean) {
        return ResponseEntity.status(resultBean.getCode())
                .body(resultBean);
    }

    @ExceptionHandler(ClientErrorException.class)
    public HttpEntity<ResultBean<Object>> clientErrorException(ClientErrorException ex) {
        return handleException(ex);
    }

    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            MissingServletRequestParameterException.class,
            JsonProcessingException.class,
    })
    public Object badRequest() {
        return handleException(ResultBean.of(400, "Bad Request"));
    }

    @ExceptionHandler(MediaTypeNotSupportedStatusException.class)
    public Object mediaTypeNotSupportedStatusException() {
        return handleException(ResultBean.of(415, "Unsupported Media Type"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Object httpRequestMethodNotSupportedException() {
        return handleException(ResultBean.of(405, "Method Not Allowed"));
    }
}
