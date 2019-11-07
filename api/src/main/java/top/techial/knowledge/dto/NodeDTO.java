package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.io.Serializable;
import java.util.List;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long id;
    private String name;
    private List<KnowledgeNode> childNodes;

    public NodeDTO(KnowledgeNode knowledgeNode) {
        this.id = knowledgeNode.getId();
        this.name = knowledgeNode.getName();
        this.childNodes = knowledgeNode.getChildNodes();
    }
}
