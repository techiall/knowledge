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

    public void setNodeId(Long nodeId) {
        this.nodeId = nodeId;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
