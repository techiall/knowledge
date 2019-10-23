package top.techial.knowledge.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer code;

    private String msg;

    private T data;

    public ResultBean() {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getTips(), null);
    }

    public ResultBean(ResultCode code) {
        this(code.getCode(), code.getTips(), null);
    }

    public ResultBean(ResultCode code, T data) {
        this(code.getCode(), code.getTips(), data);
    }

    public ResultBean(T data) {
        this(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getTips(), data);
    }

    public ResultBean(Throwable e) {
        this(ResultCode.UNKNOWN_EXCEPTION.getCode(), ResultCode.UNKNOWN_EXCEPTION.getTips(), null);
    }

}

