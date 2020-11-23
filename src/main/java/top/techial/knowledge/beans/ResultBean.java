package top.techial.knowledge.beans;

import org.springframework.lang.Nullable;

import java.io.Serializable;

/**
 * @author techial
 */
public class ResultBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Integer DEFAULT_CODE = 200;
    private static final String DEFAULT_MSG = "OK";

    private final Integer code;

    private final String msg;
    @Nullable
    private final Object data;

    private ResultBean(Integer code, String msg, @Nullable Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ResultBean ok(Object data) {
        return new ResultBean(DEFAULT_CODE, DEFAULT_MSG, data);
    }

    public static ResultBean ok() {
        return new ResultBean(DEFAULT_CODE, DEFAULT_MSG, null);
    }

    public static ResultBean of(Integer code, String msg) {
        return new ResultBean(code, msg, null);
    }

    public static ResultBean of(Integer code, String msg, Object data) {
        return new ResultBean(code, msg, data);
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Nullable
    public Object getData() {
        return data;
    }
}

