package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Labels;
import top.techial.knowledge.domain.Property;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchJsonDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private Labels labels;

    private Property property;

    private String nodeName;

    private String nodeNickName;

    private String nodeItemName;

    private String text;

}
