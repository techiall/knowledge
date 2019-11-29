package top.techial.knowledge.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import top.techial.knowledge.domain.KnowledgeNode;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author techial
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NodeVO {

    private String name;

    private Set<String> labels;

    private Map<String, String> property;

    /**
     * parentId = null -> parent
     * parentId != null && parentId is exists -> child
     * parent != null && parentId is not exists -> parent
     */
    private Long parentId;

    public KnowledgeNode toKnowledgeNode() {
        return new KnowledgeNode()
            .setName(this.name)
            .setLabels(labels == null || labels.isEmpty() ? Collections.singleton(this.name) : labels)
            .setProperty(property)
            .setParentNodeId(parentId);
    }
}
