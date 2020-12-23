package cc.techial.knowledge.web.rest.errors;

import cc.techial.knowledge.beans.ResultBean;

/**
 * @author techial
 */
public class ClientErrorException extends RuntimeException {

    private final Integer code;

    private final String msg;

    public ClientErrorException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public ResultBean getResultBean() {
        return ResultBean.of(code, msg);
    }
}
