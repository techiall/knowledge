package top.techial.knowledge.service.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.Property;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class SearchDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long nodeId;

    private Set<String> labels;

    private Map<String, List<Property.PropertyDTO>> property;

    private String nodeName;

    private String nodeItemName;

    private String text;

}
