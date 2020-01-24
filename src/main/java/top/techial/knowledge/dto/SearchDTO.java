package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private String labels;

    private String property;

    private String nodeName;

    private String nodeNickName;

    private String nodeItemName;

    private String text;

}
