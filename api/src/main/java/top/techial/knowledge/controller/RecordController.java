package top.techial.knowledge.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.techial.beans.ResultBean;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.dto.RecordDTO;
import top.techial.knowledge.mapper.RecordMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.KnowledgeNodeService;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.UserService;

import java.util.Objects;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;
    private final UserService userService;
    private final KnowledgeNodeService knowledgeNodeService;

    public RecordController(RecordService recordService, UserService userService, KnowledgeNodeService knowledgeNodeService) {
        this.recordService = recordService;
        this.userService = userService;
        this.knowledgeNodeService = knowledgeNodeService;
    }

    @GetMapping("/node/{id}")
    public ResultBean<Page<RecordDTO>> findByNodeId(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        if (!Objects.equals(knowledgeNodeService.findById(id).getUserId(), userPrincipal.getId())) {
            throw new IllegalArgumentException(String.format("nodeId: [%d] not found, userId: [%d]", id, userPrincipal.getId()));
        }
        return new ResultBean<>(recordService.findByNodeId(id, pageable)
            .map(it -> convent(it, userPrincipal)));
    }

    private RecordDTO convent(Record it, UserPrincipal userPrincipal) {
        RecordDTO result = RecordMapper.INSTANCE.toRecordDTO(it);
        return result.setUser(userService.findById(userPrincipal.getId())
            .orElseThrow(() -> new IllegalArgumentException(
                String.format("userPrincipal id [%s] not found.", userPrincipal.getId()))));
    }

}
