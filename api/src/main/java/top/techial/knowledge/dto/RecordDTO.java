package top.techial.knowledge.dto;

import lombok.Data;
import top.techial.knowledge.domain.OperatorMessageEnum;

import java.time.Instant;

/**
 * @author techial
 */
@Data
public class RecordDTO {
    private Long nodeId;

    private OperatorMessageEnum operator;

    private String message;

    private Instant createTime;

    private String nickName;

}
