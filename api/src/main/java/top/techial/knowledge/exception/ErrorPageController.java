package top.techial.knowledge.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;

/**
 * 错误页面配置
 *
 * @author techial
 */
@RestController
public class ErrorPageController implements ErrorController {

    @Override
    public String getErrorPath() {
        return "/" + "error";
    }

    @RequestMapping(value = "/error")
    public ResultBean<Object> errorGet() {
        return new ResultBean<>(ResultCode.NOT_FOUND);
    }

}
