package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchDTO {

    private Long nodeId;

    private String labels;

    private String property;

    private String nodeName;

    private String nodeNickName;

    private String nodeItemName;

    private String text;

}
