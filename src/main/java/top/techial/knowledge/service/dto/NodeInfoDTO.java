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
public class NodeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Set<String> labels;

    private String name;

    private Integer itemId;

    private Long parentNodeId;

    private Map<String, List<Property.PropertyDTO>> property;
}
