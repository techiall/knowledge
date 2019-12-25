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
import top.techial.knowledge.security.UserPrincipal;
import top.techial.knowledge.service.RecordService;
import top.techial.knowledge.service.UserService;

/**
 * @author techial
 */
@RestController
@RequestMapping("/api/record")
public class RecordController {
    private final RecordService recordService;
    private final UserService userService;

    public RecordController(RecordService recordService, UserService userService) {
        this.recordService = recordService;
        this.userService = userService;
    }

    @GetMapping("/node/{id}")
    public ResultBean<Page<RecordDTO>> findByNodeId(
        @PathVariable Long id,
        @AuthenticationPrincipal UserPrincipal userPrincipal,
        @PageableDefault(sort = "createTime", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        return new ResultBean<>(recordService.findByNodeId(id, userPrincipal.getId(), pageable).map(this::convent));
    }

    private RecordDTO convent(Record record) {
        RecordDTO dto = new RecordDTO(record);
        // FIXME
//        dto.setUser(userService.findById(record.getUserId()).orElseThrow(NullPointerException::new));
        return dto;
    }
}
