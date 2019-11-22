package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Operator;
import top.techial.knowledge.domain.Record;

import java.io.Serializable;
import java.util.Date;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String id;
    private Long nodeId;
    private Integer userId;
    private Operator operator;
    private Date createTime;

    public RecordDTO(Record record) {
        this.id = record.getId();
        this.nodeId = record.getNodeId();
        this.userId = record.getUserId();
        this.operator = record.getOperator();
        this.createTime = record.getCreateTime();
    }
}
