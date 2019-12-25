package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.OperatorMessageEnum;
import top.techial.knowledge.domain.User;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long nodeId;
    private User user;
    private OperatorMessageEnum operator;
    private OperatorMessageEnum message;
    private Instant createTime;
    private Instant updateTime;
}
