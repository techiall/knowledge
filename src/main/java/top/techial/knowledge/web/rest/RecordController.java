package top.techial.knowledge.web.rest;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.techial.knowledge.beans.ResultBean;
import top.techial.knowledge.repository.RecordRepository;
import top.techial.knowledge.service.mapper.RecordMapper;

/**
 * 节点操作记录
 *
 * @author techial
 */
@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordRepository recordRepository;
    private final RecordMapper recordMapper;

    public RecordController(RecordRepository recordRepository, RecordMapper recordMapper) {
        this.recordRepository = recordRepository;
        this.recordMapper = recordMapper;
    }

    @GetMapping("/node/{id}")
    @PreAuthorize("hasAnyAuthority(#id.toString())")
    public ResultBean findByNodeId(
            @PathVariable Long id,
            @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return ResultBean.ok(recordRepository.findAllByNodeId(id, pageable).map(recordMapper::toRecordDTO));
    }
}
