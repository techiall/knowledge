package top.techial.knowledge.domain;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

/**
 * @author techial
 */
@Data
@Document
@Accessors(chain = true)
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    private Long nodeId;

    private Integer userId;

    private Object input;

    private Object result;

    private Operator operator;

    @CreatedDate
    private Date createTime;

}
