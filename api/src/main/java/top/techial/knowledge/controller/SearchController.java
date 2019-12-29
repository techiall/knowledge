package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.KnowledgeNode;
import top.techial.knowledge.domain.User;
import top.techial.knowledge.dto.NodeBaseDTO;
import top.techial.knowledge.mapper.KnowledgeNodeMapper;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.NodeTextService;
import top.techial.knowledge.service.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final KnowledgeNodeService knowledgeNodeService;
    private final NodeTextService nodeTextService;
    private final UserService userService;

    public SearchController(KnowledgeNodeService knowledgeNodeService, NodeTextService nodeTextService, UserService userService) {
        this.knowledgeNodeService = knowledgeNodeService;
        this.nodeTextService = nodeTextService;
        this.userService = userService;
    }

    @GetMapping
    public ResultBean<Object> search(
        @RequestParam(name = "q") String question,
        @RequestParam(required = false, defaultValue = "false") Boolean tips,
        @PageableDefault Pageable pageable
    ) {
        question = String.format("*%s*", question);
        if (Boolean.TRUE.equals(tips)) {
            return new ResultBean<>(knowledgeNodeService.findAllByNameLike(question, pageable)
                .map(KnowledgeNodeMapper.INSTANCE::toNodeBaseDTO));
        }
        Page<Map<Long, Object>> result = knowledgeNodeService.findAllByNameLike(question, pageable)
            .map(this::convent);
        return new ResultBean<>(result);
    }

    private Map<Long, Object> convent(KnowledgeNode it) {
        Map<String, List<NodeBaseDTO>> result = knowledgeNodeService.getChildAndParent(it.getId(), 10);
        String text = nodeTextService.findById(it.getId()).getText();

        if (text == null) {
            text = "";
        }
        text = text.replaceAll("<[^>]*>", "");
        text = text.substring(0, Math.min(200, text.length()));

        String user = userService.findById(it.getUserId()).orElse(new User()).getNickName();
        Map<String, Object> map = new HashMap<>();
        map.put("node", KnowledgeNodeMapper.INSTANCE.toNodeInfoDTO(it));
        map.put("info", result);
        map.put("text", text);
        map.put("user", user);
        return Collections.singletonMap(it.getId(), map);
    }

}
