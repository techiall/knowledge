package top.techial.knowledge.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author techial
 */
@Data
public class RecordDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private String operator;

    private String message;

    private Instant createTime;

    private String nickName;

}
