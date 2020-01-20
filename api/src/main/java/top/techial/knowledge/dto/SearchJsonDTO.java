package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Property;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchJsonDTO {

    private Long nodeId;

    private Labels labels;

    private Property property;

    private String nodeName;

    private String nodeNickName;

    private String nodeItemName;

    private String text;

}
