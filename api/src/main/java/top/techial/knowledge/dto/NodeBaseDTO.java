package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.io.Serializable;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeBaseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long parentNodeId;

    private final Long id;

    private final String name;

    public NodeBaseDTO(KnowledgeNode knowledgeNode) {
        this.parentNodeId = knowledgeNode.getParentNodeId();
        this.id = knowledgeNode.getId();
        this.name = knowledgeNode.getName();
    }
}
