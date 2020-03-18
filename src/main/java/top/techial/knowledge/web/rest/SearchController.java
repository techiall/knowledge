package top.techial.knowledge.web.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Item;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.dto.SearchDTO;
import top.techial.knowledge.service.mapper.NodeMapper;
import top.techial.knowledge.web.rest.errors.ItemNotFoundException;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private static final String REGEX = "<[^>]*>|&nbsp;";
    private final NodeMapper nodeMapper;
    private final NodeService nodeService;
    private final ItemRepository itemRepository;

    public SearchController(NodeMapper nodeMapper, NodeService nodeService, ItemRepository itemRepository) {
        this.nodeMapper = nodeMapper;
        this.nodeService = nodeService;
        this.itemRepository = itemRepository;
    }

    private static String text(String text) {
        text = text == null ? "" : text;
        text = text.replaceAll(REGEX, "").trim();
        text = text.substring(0, Math.min(200, text.length()));
        return text;
    }

    @GetMapping
    public ResultBean<Object> search(
            @RequestParam(name = "q") String question,
            @RequestParam(required = false, defaultValue = "false") Boolean tips,
            @PageableDefault Pageable pageable
    ) {
        question = question.substring(0, Math.min(32, question.length()));
        if (Boolean.TRUE.equals(tips)) {
            return ResultBean.ok(nodeService
                    .findContentByNameLike(question, PageRequest.of(0, 10, Sort.by("updateTime").descending()))
                    .map(nodeMapper::toNodeInfoDTO)
                    .getContent());
        }
        int page = Math.min(50, pageable.getPageNumber());
        int size = Math.min(20, pageable.getPageSize());
        return ResultBean.ok(nodeService.findContentByNameLike(
                question, PageRequest.of(page, size, pageable.getSort())).map(this::convent));
    }

    private SearchDTO convent(Node node) {
        Item item = itemRepository.findById(node.getItemId())
                .orElseThrow(ItemNotFoundException::new);
        SearchDTO result = nodeMapper.toSearchDTO(node, item);
        return result.setText(text(result.getText()));
    }

}
