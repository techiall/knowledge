package top.techial.beans;

import java.io.Serializable;

/**
 * @author techial
 */
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code = ResultCode.SUCCESS.getCode();

    private String msg = ResultCode.SUCCESS.getTips();

    private T data;

    public ResultBean(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public ResultBean() {
    }

    public ResultBean(ResultCode code) {
        this(code.getCode(), code.getTips(), null);
    }

    public ResultBean(ResultCode code, T data) {
        this(code.getCode(), code.getTips(), data);
    }

    public ResultBean(T data) {
        this(ResultCode.SUCCESS, data);
    }

    public T getData() {
        return data;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

