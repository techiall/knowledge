package top.techial.knowledge.beans;

import java.io.Serializable;

/**
 * @author techial
 */
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Integer DEFAULT_CODE = 200;
    private static final String DEFAULT_MSG = "OK";

    private final Integer code;

    private final String msg;

    private final T data;

    private ResultBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> ResultBean<T> ok(T data) {
        return new ResultBean<>(DEFAULT_CODE, DEFAULT_MSG, data);
    }

    public static <T> ResultBean<T> ok() {
        return new ResultBean<>(DEFAULT_CODE, DEFAULT_MSG, null);
    }

    public static <T> ResultBean<T> of(Integer code, String msg) {
        return new ResultBean<>(code, msg, null);
    }

    public static <T> ResultBean<T> of(Integer code, String msg, T data) {
        return new ResultBean<>(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }
}

