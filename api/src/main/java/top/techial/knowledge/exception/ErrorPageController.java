package top.techial.knowledge.exception;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import top.techial.beans.ResultBean;
import top.techial.beans.ResultCode;

/**
 * 错误页面配置
 *
 * @author techial
 */
@RestController
@Controller
public class ErrorPageController implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }

    @GetMapping(value = ERROR_PATH)
    public ResultBean<Object> errorGet() {
        return new ResultBean<>(ResultCode.NOT_FOUND);
    }

}
