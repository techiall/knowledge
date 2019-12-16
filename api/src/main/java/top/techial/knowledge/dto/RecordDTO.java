package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Operator;
import top.techial.knowledge.domain.Record;
import top.techial.knowledge.domain.User;

import java.io.Serializable;
import java.util.Date;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long nodeId;
    private User user;
    private Operator operator;
    private Date createTime;

    public RecordDTO(Record record) {
        this.nodeId = record.getNodeId();
        this.operator = record.getOperator();
        this.createTime = record.getCreateTime();
    }
}
