package top.techial.knowledge.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Property;
import top.techial.knowledge.dto.SearchDTO;
import top.techial.knowledge.dto.SearchJsonDTO;
import top.techial.knowledge.service.NodeService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/search")
public class SearchController {
    private final NodeService nodeService;
    private final ObjectMapper objectMapper;
    private static final String REGEX = "<[^>]*>|&nbsp;";

    public SearchController(NodeService nodeService, ObjectMapper objectMapper) {
        this.nodeService = nodeService;
        this.objectMapper = objectMapper;
    }

    @GetMapping
    public ResultBean<Object> search(
            @RequestParam(name = "q") String question,
            @RequestParam(required = false, defaultValue = "false") Boolean tips,
            @PageableDefault Pageable pageable
    ) {
        if (Boolean.TRUE.equals(tips)) {
            return new ResultBean<>(nodeService.findByNameLike(question));
        }
        return new ResultBean<>(convent(nodeService.findContentByNameLike(question, pageable)));
    }

    private List<SearchJsonDTO> convent(PageImpl<SearchDTO> list) {
        return list.stream()
                .map(this::toSearchJson)
                .collect(Collectors.toList());
    }

    @SneakyThrows
    private SearchJsonDTO toSearchJson(SearchDTO searchDTO) {
        return new SearchJsonDTO()
                .setNodeId(searchDTO.getNodeId())
                .setLabels(objectMapper.readValue(searchDTO.getLabels(), Labels.class))
                .setProperty(objectMapper.readValue(searchDTO.getProperty(), Property.class))
                .setNodeName(searchDTO.getNodeName())
                .setNodeNickName(searchDTO.getAuthorNickname())
                .setNodeItemName(searchDTO.getItemName())
                .setText(text(searchDTO.getText()));
    }

    private static String text(String text) {
        text = text == null ? "" : text;
        text = text.replaceAll(REGEX, "").trim();
        text = text.substring(0, Math.min(200, text.length()));
        return text;
    }

}
