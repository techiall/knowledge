package top.techial.beans;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public ResultBean() {
        this.msg = ResultCode.SUCCESS.getTips();
        this.code = ResultCode.SUCCESS.getCode();
        this.data = null;
    }

    public ResultBean(ResultCode code) {
        this.msg = code.getTips();
        this.code = code.getCode();
        this.data = null;
    }

    public ResultBean(ResultCode code, T data) {
        this.msg = code.getTips();
        this.code = code.getCode();
        this.data = data;
    }

    public ResultBean(T data) {
        this.msg = ResultCode.SUCCESS.getTips();
        this.code = ResultCode.SUCCESS.getCode();
        this.data = data;
    }

    public ResultBean(Throwable e) {
        this.code = ResultCode.UNKNOWN_EXCEPTION.getCode();
        this.msg = ResultCode.UNKNOWN_EXCEPTION.getTips();
        this.data = null;
    }

}

