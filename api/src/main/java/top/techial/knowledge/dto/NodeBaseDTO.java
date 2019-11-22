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

    private Long id;

    private String name;

    private Boolean isParentNode;

    public NodeBaseDTO(KnowledgeNode knowledgeNode) {
        this.id = knowledgeNode.getId();
        this.name = knowledgeNode.getName();
        this.isParentNode = knowledgeNode.getIsParentNode();
    }
}
