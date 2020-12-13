package top.techial.knowledge.web.rest;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Node;
import top.techial.knowledge.repository.ItemRepository;
import top.techial.knowledge.service.NodeService;
import top.techial.knowledge.service.dto.SearchDTO;
import top.techial.knowledge.service.mapper.NodeMapper;
import top.techial.knowledge.web.rest.errors.ItemNotFoundException;

import java.util.Optional;

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

    public SearchController(NodeMapper nodeMapper,
                            NodeService nodeService,
                            ItemRepository itemRepository) {
        this.nodeMapper = nodeMapper;
        this.nodeService = nodeService;
        this.itemRepository = itemRepository;
    }

    private static String textFilter(String text) {
        return Optional.ofNullable(text)
                       .map(it -> it.replaceAll(REGEX, "").trim())
                       .map(s -> s.substring(0, Math.min(200, s.length())))
                       .orElse("");
    }

    @GetMapping
    public ResultBean search(@RequestParam(name = "q") String question,
                             @RequestParam(required = false, defaultValue = "false") boolean tips,
                             @PageableDefault Pageable pageable) {
        question = question.substring(0, Math.min(32, question.length()));
        if (Boolean.TRUE.equals(tips)) {
            return ResultBean.ok(nodeService.findContentByNameLike(question, PageRequest.of(0, 10))
                                            .map(nodeMapper::toNodeInfoDTO)
                                            .getContent());
        }
        final int page = Math.min(50, pageable.getPageNumber());
        final int size = Math.min(20, pageable.getPageSize());
        return ResultBean.ok(nodeService.findContentByNameLike(
                question, PageRequest.of(page, size, pageable.getSort())).map(this::convent));
    }

    private SearchDTO convent(Node node) {
        final var result = itemRepository.findById(node.getItemId())
                                         .map(it -> nodeMapper.toSearchDTO(node, it))
                                         .orElseThrow(ItemNotFoundException::new);
        final var textFilter = textFilter(result.getText());
        result.setText(textFilter);
        return result;
    }
}
