package top.techial.knowledge.dto;

import lombok.Data;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
public class NodeDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long parentNodeId;

    private final Long id;

    private final String name;

    private final List<NodeDTO> childNodes;

    public NodeDTO(KnowledgeNode knowledgeNode) {
        this.id = knowledgeNode.getId();
        this.name = knowledgeNode.getName();
        this.parentNodeId = knowledgeNode.getParentNodeId();
        this.childNodes = knowledgeNode.getChildNodes().stream()
            .sorted(Comparator.comparing(KnowledgeNode::getCreateTime))
            .map(NodeDTO::new).collect(Collectors.toList());
    }
}
