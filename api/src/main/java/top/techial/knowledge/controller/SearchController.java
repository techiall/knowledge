package top.techial.knowledge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.techial.beans.ResultBean;
import top.techial.knowledge.service.NodeService;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final NodeService nodeService;
    private static final String REGEX = "<[^>]*>|&nbsp;";

    public SearchController(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    @GetMapping
    public ResultBean<Object> search(
            @RequestParam(name = "q") String question,
            @RequestParam(required = false, defaultValue = "false") Boolean tips
    ) {
        question = String.format("*%s*", question);
        if (Boolean.TRUE.equals(tips)) {
            return new ResultBean<>(nodeService.findByNameLike(question));
        }
        return new ResultBean<>(nodeService.findContentByNameLike(question));
    }

    private static String getText(String text) {
        text = text == null ? "" : text;
        text = text.replaceAll(REGEX, "").trim();
        text = text.substring(0, Math.min(200, text.length()));
        return text;
    }

}
