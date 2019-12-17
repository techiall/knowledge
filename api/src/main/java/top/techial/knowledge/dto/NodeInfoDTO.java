package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long parentNodeId;
    private Long id;
    private Set<String> labels;
    private String name;
    private String sortId;
    private String userId;
    private Map<String, String> property;

}
