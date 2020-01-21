package top.techial.knowledge.dto;

import lombok.Data;
import top.techial.knowledge.domain.OperatorMessageEnum;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
public class RecordDTO implements Serializable {
    private Long nodeId;

    private OperatorMessageEnum operator;

    private String message;

    private String content;

    private Instant createTime;

    private String nickName;

}
