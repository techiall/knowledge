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
    private final Long parentNodeId;
    private final Long id;
    private final Set<String> labels;
    private final String name;
    private final String sortId;
    private final String userId;
    private final Map<String, String> property;

    public NodeInfoDTO(KnowledgeNode knowledgeNode) {
        this.id = knowledgeNode.getId();
        this.userId = knowledgeNode.getUserId();
        this.labels = knowledgeNode.getLabels();
        this.name = knowledgeNode.getName();
        this.sortId = knowledgeNode.getSortId();
        this.property = knowledgeNode.getProperty();
        this.parentNodeId = knowledgeNode.getParentNodeId();
    }

}
