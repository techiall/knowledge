package top.techial.knowledge.service.dto;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private String operator;

    private String message;

    private Instant createTime;

    private String nickName;

    public Long getNodeId() {
        return nodeId;
    }

    public RecordDTO setNodeId(Long nodeId) {
        this.nodeId = nodeId;
        return this;
    }

    public String getOperator() {
        return operator;
    }

    public RecordDTO setOperator(String operator) {
        this.operator = operator;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RecordDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public RecordDTO setCreateTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getNickName() {
        return nickName;
    }

    public RecordDTO setNickName(String nickName) {
        this.nickName = nickName;
        return this;
    }
}
