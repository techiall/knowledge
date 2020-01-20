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
import top.techial.knowledge.dto.RecordDTO;
import top.techial.knowledge.mapper.RecordMapper;
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.RecordService;

/**
 * 节点操作记录
 *
 * @author techial
 */
@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;

    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/node/{id}")
    public ResultBean<Page<RecordDTO>> findByNodeId(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResultBean<>(recordService.findByNodeId(id, pageable).map(RecordMapper.INSTANCE::toRecordDTO));
    }

}
