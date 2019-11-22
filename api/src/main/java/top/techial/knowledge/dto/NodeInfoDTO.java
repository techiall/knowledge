package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

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
    private final Boolean isParentNode;
    private Long id;
    private Set<String> labels;
    private String name;
    private Map<String, String> property;

    public NodeInfoDTO(KnowledgeNode knowledgeNode) {
        this.id = knowledgeNode.getId();
        this.labels = knowledgeNode.getLabels();
        this.name = knowledgeNode.getName();
        this.property = knowledgeNode.getProperty();
        this.isParentNode = knowledgeNode.getIsParentNode();
    }

}
